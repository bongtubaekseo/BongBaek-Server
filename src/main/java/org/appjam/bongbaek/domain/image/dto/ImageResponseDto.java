package org.appjam.bongbaek.domain.image.dto;

import org.appjam.bongbaek.domain.image.entity.Image;

import java.util.List;
import java.util.stream.Collectors;

public record ImageResponseDto(
        String imageId,
        String imageUrl
) {
    public static ImageResponseDto from(Image image) {
        return new ImageResponseDto(image.getImageId(), image.getImageUrl());
    }

    public static List<ImageResponseDto> fromList(List<Image> images) {
        return images.stream()
                .map(ImageResponseDto::from)
                .collect(Collectors.toList());
    }
}
