package org.appjam.bongbaek.domain.image.controller;

import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.domain.image.dto.ImageUploadRequestDto;
import org.appjam.bongbaek.domain.image.service.ImageService;
import org.appjam.bongbaek.global.api.code.image.SuccessCode;
import org.appjam.bongbaek.global.api.response.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RestController("/api/v1/image")
public class ImageController {
    private final ImageService imageService;

    @PostMapping
    public ApiResponse uploadImage(
            @RequestBody ImageUploadRequestDto imageUploadRequestDto
            ) {
        imageService.uploadImage(imageUploadRequestDto);

        return ApiResponse.success(SuccessCode.IMAGE_CREATED);
    }

    @GetMapping("/{imageId}")
    public ApiResponse getImageByIdImageId(
            @PathVariable String imageId
    ) {

        return ApiResponse.success(
                SuccessCode.IMAGE_FOUND,
                imageService.findImageByImageId(imageId));
    }

    @GetMapping("/owner/{ownerId}")
    public ApiResponse getAllImageByOwnerId(
        @PathVariable String ownerId
    ) {

        return ApiResponse.success(
                SuccessCode.IMAGE_FOUND,
                imageService.findImagesByOwnerIdOrderBySequence(ownerId));
    }

    @DeleteMapping("/{imageId}")
    public ApiResponse deleteImage(
            @PathVariable String imageId
    ) {
        imageService.deleteImage(imageId);

        return ApiResponse.success(SuccessCode.IMAGE_DELETED);
    }
}
