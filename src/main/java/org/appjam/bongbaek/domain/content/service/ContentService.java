package org.appjam.bongbaek.domain.content.service;

import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.domain.content.dto.request.ContentWriteDto;
import org.appjam.bongbaek.domain.content.dto.response.ContentDetailResponseDto;
import org.appjam.bongbaek.domain.content.dto.response.ContentHomeResponseDto;
import org.appjam.bongbaek.domain.content.dto.response.ContentListDto;
import org.appjam.bongbaek.domain.content.entity.Content;
import org.appjam.bongbaek.domain.content.entity.ContentImage;
import org.appjam.bongbaek.domain.content.repository.ContentRepository;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.appjam.bongbaek.domain.image.entity.OwnerType;
import org.appjam.bongbaek.global.exception.common.RequestInvalidException;
import org.appjam.bongbaek.global.exception.content.ContentNotFoundException;
import org.appjam.bongbaek.global.exception.image.ImageNotFoundException;
import org.appjam.bongbaek.global.s3.dto.FileDto;
import org.appjam.bongbaek.global.s3.uploader.FileUploader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final int PAGE_SIZE = 5;

    private final ContentRepository contentRepository;
    private final FileUploader fileUploader;

    @Transactional(readOnly = true)
    public ContentDetailResponseDto getContentDetail(String contentId) {
        Content content = contentRepository.findContentByIdWithImages(contentId)
                .orElseThrow(ContentNotFoundException::new);

        return ContentDetailResponseDto.from(content);
    }

    @Transactional(readOnly = true)
    public ContentHomeResponseDto getContentForHome() {
        List<Content> contents = contentRepository.findTop3ByOrderByCreatedDateTimeDesc();

        return ContentHomeResponseDto.from(contents);
    }

    @Transactional(readOnly = true)
    public ContentListDto getContentList(int page, String category) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Category contentCategory = Category.of(category).orElse(null);

        if (contentCategory == null) {
            Page<Content> allContents = contentRepository.findAllByOrderByCreatedDateTimeDesc(pageable);
            return ContentListDto.of(allContents);
        }

        Page<Content> categoryContents = contentRepository.findContentsByContentCategoryOrderByCreatedDateTimeDesc(contentCategory, pageable);
        return ContentListDto.of(categoryContents);
    }

    @Transactional
    public void createContent(ContentWriteDto request, MultipartFile thumbnailFile) {
        Category contentCategory = Category.of(request.contentCategory())
                .orElseThrow(RequestInvalidException::new);

        FileDto thumbnailDto = fileUploader.upload(thumbnailFile, OwnerType.CONTENT);

        try {
            Content content = Content.builder()
                    .contentTitle(request.contentTitle())
                    .contentCategory(contentCategory)
                    .thumbnailUrl(thumbnailDto.imageUrl())
                    .build();

            ContentImage thumbnail = ContentImage.createThumbnail(thumbnailDto);

            content.addContentImage(thumbnail);
            contentRepository.save(content);

        } catch(Exception e) {
            if (thumbnailDto != null) {
                fileUploader.delete(thumbnailDto.storageKey());
            }
            throw new RequestInvalidException();
        }
    }

    @Transactional
    public void updateThumbnail(String contentId, MultipartFile newThumbnailFile) {
        Content content = contentRepository.findContentByIdWithImages(contentId)
                .orElseThrow(ContentNotFoundException::new);

        ContentImage oldThumbnail = content.getThumbnail()
                .orElseThrow(ImageNotFoundException::new);

        FileDto newThumbnailDto = fileUploader.upload(newThumbnailFile, OwnerType.CONTENT);

        try {
            ContentImage newThumbnail = ContentImage.createThumbnail(newThumbnailDto);

            content.addContentImage(newThumbnail);
            content.updateThumbnailUrl(newThumbnail.getImageUrl());
            content.getContentImages().remove(oldThumbnail);

        } catch (Exception e) {
            if(newThumbnailDto != null) {
                fileUploader.delete(newThumbnailDto.storageKey());
            }
            throw new RequestInvalidException();
        }
        fileUploader.delete(oldThumbnail.getStorageKey());
    }

    // TO DO: 메인 이미지 개별 삭제 기능 추가 + Sequence 정렬까지
    @Transactional
    public void uploadMainImage(String contentId, MultipartFile newImageFile) {
        Content content = contentRepository.findContentByIdWithImages(contentId)
                .orElseThrow(ContentNotFoundException::new);

        FileDto newMainImageDto = fileUploader.upload(newImageFile, OwnerType.CONTENT);

        try {
            int nextSequence = content.getContentImages().size();

            ContentImage newMainImage = ContentImage.createMainImage(newMainImageDto, nextSequence);
            content.addContentImage(newMainImage);

        } catch (Exception e) {
            if (newMainImageDto != null) {
                fileUploader.delete(newMainImageDto.storageKey());
            }
            throw new RequestInvalidException();
        }
    }

    // TO DO: FileUploader 수정 후 반복문 삭제
    @Transactional
    public void deleteContent(String contentId) {
        Content content = contentRepository.findContentByIdWithImages(contentId)
                .orElseThrow(ContentNotFoundException::new);

        contentRepository.delete(content);

        content.getContentImages()
                .forEach(image -> fileUploader.delete(image.getStorageKey()));
    }
}
