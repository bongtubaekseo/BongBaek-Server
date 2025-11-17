package org.appjam.bongbaek.domain.content.service;

import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.domain.content.dto.response.ContentHomeResponseDto;
import org.appjam.bongbaek.domain.content.dto.response.ContentListDto;
import org.appjam.bongbaek.domain.content.dto.request.ContentWriteDto;
import org.appjam.bongbaek.domain.content.dto.response.ContentDetailResponseDto;
import org.appjam.bongbaek.domain.content.entity.Content;
import org.appjam.bongbaek.domain.content.entity.ContentImage;
import org.appjam.bongbaek.domain.content.repository.ContentRepository;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.appjam.bongbaek.global.exception.common.RequestInvalidException;
import org.appjam.bongbaek.global.exception.content.ContentNotFoundException;
import org.appjam.bongbaek.global.s3.dto.FileDto;
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
    private final ContentImageService contentImageService;

    @Transactional
    public void createContent(
            ContentWriteDto request,
            MultipartFile thumbnailFile
    ) {
        Category category = Category.of(request.contentCategory()).orElseThrow(RequestInvalidException::new);

        FileDto thumbnailDto = contentImageService.uploadImage(thumbnailFile);

        Content content = Content.builder()
                .contentTitle(request.contentTitle())
                .contentCategory(category)
                .thumbnailUrl(thumbnailDto.imageUrl())
                .thumbnailStorageKey(thumbnailDto.storageKey())
                .build();

        contentRepository.save(content);
    }


    @Transactional
    public void updateThumbnail(String contentId, MultipartFile newThumbnailFile) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);

        FileDto newThumbnailDto = contentImageService.uploadImage(newThumbnailFile);

        content.updateThumbnail(newThumbnailDto.imageUrl(), newThumbnailDto.storageKey());
        contentImageService.deleteImage(content.getThumbnailStorageKey());
    }

    @Transactional
    public void uploadMainImage(String contentId, MultipartFile newImageFile) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);

        if (content.getContentImage() != null) {
            String oldS3Key = content.getContentImage().getStorageKey();
            contentImageService.deleteImage(oldS3Key);
        }

        FileDto newMainImageDto = contentImageService.uploadImage(newImageFile);

        ContentImage newMainImage = ContentImage.builder()
                .imageUrl(newMainImageDto.imageUrl())
                .storageKey(newMainImageDto.storageKey())
                .build();

        content.uploadContentImage(newMainImage);
    }

    @Transactional
    public void deleteContent(String contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);

        String thumbnailS3Key = content.getThumbnailStorageKey();
        contentImageService.deleteImage(thumbnailS3Key);

        if (content.getContentImage() != null) {
            String mainImageS3Key = content.getContentImage().getStorageKey();
            contentImageService.deleteImage(mainImageS3Key);
        }

        contentRepository.delete(content);
    }

    @Transactional(readOnly = true)
    public ContentDetailResponseDto getContentDetail(String contentId) {
        Content content = contentRepository.findByIdWithContentImage(contentId)
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

        if(contentCategory == null) {
            Page<Content> contents = contentRepository.findAllByOrderByCreatedDateTimeDesc(pageable);

            return ContentListDto.of(contents);
        }

        Page<Content> contents = contentRepository.findContentsByContentCategoryOrderByCreatedDateTimeDesc(
                Category.of(category).orElse(null),
                pageable
        );

        return ContentListDto.of(contents);
    }
}
