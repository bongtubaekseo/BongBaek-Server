package org.appjam.bongbaek.domain.content.service;

import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.domain.content.dto.ContentWriteDto;
import org.appjam.bongbaek.domain.content.dto.ContentDetailResponseDto;
import org.appjam.bongbaek.domain.content.entity.Content;
import org.appjam.bongbaek.domain.content.entity.ContentImage;
import org.appjam.bongbaek.domain.content.repository.ContentRepository;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.appjam.bongbaek.global.exception.common.RequestInvalidException;
import org.appjam.bongbaek.global.exception.content.ContentNotFoundException;
import org.appjam.bongbaek.global.exception.image.ImageNotFoundException;
import org.appjam.bongbaek.global.s3.dto.FileDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final ContentImageService contentImageService;

    @Transactional
    public void createContent(
            ContentWriteDto request,
            MultipartFile thumbnailFile
    ) {
        FileDto thumbnailDto = contentImageService.uploadImage(thumbnailFile);

        Content content = Content.builder()
                .contentTitle(request.contentTitle())
                .contentCategory(Category.of(request.contentCategory()).orElseThrow(RequestInvalidException::new))
                .thumbnailUrl(thumbnailDto.imageUrl())
                .thumbnailStorageKey(thumbnailDto.storageKey())
                .build();

        contentRepository.save(content);
    }


    @Transactional
    public void updateThumbnail(String contentId, MultipartFile newThumbnailFile) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);

        String oldS3Key = content.getThumbnailStorageKey();
        if(oldS3Key == null){
            throw new ImageNotFoundException();
        }

        FileDto newThumbnailDto = contentImageService.uploadImage(newThumbnailFile);

        content.updateThumbnail(newThumbnailDto.imageUrl(), newThumbnailDto.storageKey());

        contentImageService.deleteImage(oldS3Key);
    }

    @Transactional
    public void uploadMainImage(String contentId, MultipartFile mainImageFile) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);

        String oldS3Key = content.getContentImage().getStorageKey();
        FileDto newMainImageDto = contentImageService.uploadImage(mainImageFile);

        ContentImage newMainImage = ContentImage.builder()
                .imageUrl(newMainImageDto.imageUrl())
                .storageKey(newMainImageDto.storageKey())
                .build();

        content.uploadContentImage(newMainImage);
        contentImageService.deleteImage(oldS3Key);
    }

    @Transactional
    public void deleteContent(String contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);

        String thumbnailS3Key = content.getThumbnailStorageKey();
        String mainImageS3Key = content.getContentImage().getStorageKey();

        contentImageService.deleteImage(thumbnailS3Key);
        contentImageService.deleteImage(mainImageS3Key);

        contentRepository.delete(content);
    }

    @Transactional(readOnly = true)
    public ContentDetailResponseDto getContentDetail(String contentId) {
        Content content = contentRepository.findByIdWithContentImage(contentId)
                .orElseThrow(ContentNotFoundException::new);

        return ContentDetailResponseDto.from(content);
    }
}
