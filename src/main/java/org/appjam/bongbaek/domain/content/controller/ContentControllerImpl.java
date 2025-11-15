package org.appjam.bongbaek.domain.content.controller;

import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.domain.content.dto.ContentWriteDto;
import org.appjam.bongbaek.domain.content.dto.ContentDetailResponseDto;
import org.appjam.bongbaek.domain.content.service.ContentService;
import org.appjam.bongbaek.global.api.code.content.SuccessCode;
import org.appjam.bongbaek.global.api.response.ApiResponse;
import org.appjam.bongbaek.global.api.response.SuccessResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/content")
public class ContentControllerImpl implements ContentController {

    private final ContentService contentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<Void> createContent(
            @RequestPart("request") ContentWriteDto request,
            @RequestPart("thumbnail") MultipartFile thumbnailFile
    ) {
        contentService.createContent(request, thumbnailFile);

        return ApiResponse.success(SuccessCode.CONTENT_CREATED);
    }

    @PutMapping(value = "/{contentId}/thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<Void> updateThumbnail(
            @PathVariable String contentId,
            @RequestPart("thumbnail") MultipartFile newThumbnailFile
    ) {
        contentService.updateThumbnail(contentId, newThumbnailFile);

        return ApiResponse.success(SuccessCode.CONTENT_THUMBNAIL_UPDATED);
    }

    @PostMapping(value = "/{contentId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<Void> uploadMainImage(
            @PathVariable String contentId,
            @RequestPart("main_image") MultipartFile mainImageFile
    ) {
        contentService.uploadMainImage(contentId, mainImageFile);

        return ApiResponse.success(SuccessCode.CONTENT_MAIN_IMAGE_UPLOADED);
    }

    @DeleteMapping("/{contentId}")
    public SuccessResponse<Void> deleteContent(@PathVariable String contentId) {
        contentService.deleteContent(contentId);

        return ApiResponse.success(SuccessCode.CONTENT_DELETED);
    }

    @GetMapping("/{contentId}")
    public SuccessResponse<ContentDetailResponseDto> getContentDetail(
            @PathVariable String contentId
    ) {
        ContentDetailResponseDto response = contentService.getContentDetail(contentId);
        return ApiResponse.success(SuccessCode.CONTENT_FOUND, response);
    }
}
