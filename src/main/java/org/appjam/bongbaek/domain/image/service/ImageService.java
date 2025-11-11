package org.appjam.bongbaek.domain.image.service;

import org.appjam.bongbaek.domain.image.dto.ImageResponseDto;
import org.appjam.bongbaek.domain.image.dto.ImageUploadRequestDto;

import java.util.List;

public interface ImageService {
    void uploadImage(ImageUploadRequestDto requestDto);

    void deleteImage(String imageId);

    ImageResponseDto findImageByImageId(String imageId);

    List<ImageResponseDto> findImagesByOwnerIdOrderBySequence(String ownerId);
}
