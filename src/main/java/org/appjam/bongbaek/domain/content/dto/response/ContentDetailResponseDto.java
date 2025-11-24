package org.appjam.bongbaek.domain.content.dto.response;

import org.appjam.bongbaek.domain.content.entity.Content;
import org.appjam.bongbaek.domain.content.entity.ContentImage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record ContentDetailResponseDto(
        String contentId,
        String contentTitle,
        String contentCategory,
        List<String> imageUrls,
        String createdAt
) {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static ContentDetailResponseDto from(Content content) {
        String createdAt = content.getCreatedDateTime()
                .format(DATE_FORMATTER);

        List<String> imageUrls = new ArrayList<>();
        imageUrls.add(content.getThumbnailUrl());

        List<String> mainImageUrls = content.getContentImages().stream()
                .filter(image -> !image.isThumbnail())
                .sorted(Comparator.comparing(ContentImage::getSequence))
                .map(ContentImage::getImageUrl)
                .toList();

        imageUrls.addAll(mainImageUrls);

        return new ContentDetailResponseDto(
                content.getContentId(),
                content.getContentTitle(),
                content.getContentCategory().getDescription(),
                imageUrls,
                createdAt
        );
    }
}
