package org.appjam.bongbaek.domain.content.dto.response;

import org.appjam.bongbaek.domain.content.entity.Content;

import java.time.format.DateTimeFormatter;

public record ContentDetailResponseDto(
            String contentId,
            String contentTitle,
            String contentCategory,
            String thumbnailUrl,
            String mainImageUrl,
            String createdAt
) {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static ContentDetailResponseDto from(Content content) {
        String createdAt = content.getCreatedDateTime()
                .format(DATE_FORMATTER);

        return new ContentDetailResponseDto(
                content.getContentId(),
                content.getContentTitle(),
                content.getContentCategory().getDescription(),
                content.getThumbnailUrl(),
                content.getMainImageUrl(),
                createdAt
        );
    }
}

