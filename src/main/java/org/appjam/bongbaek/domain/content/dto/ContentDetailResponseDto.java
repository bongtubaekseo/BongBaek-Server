package org.appjam.bongbaek.domain.content.dto;

import org.appjam.bongbaek.domain.content.entity.Content;
import org.appjam.bongbaek.domain.event.entity.Category;

import java.time.LocalDateTime;

public record ContentDetailResponseDto(
            String contentId,
            String contentTitle,
            Category contentCategory,
            String thumbnailUrl,
            String mainImageUrl,
            LocalDateTime createdAt
) {
    public static ContentDetailResponseDto from(Content content) {
        return new ContentDetailResponseDto(
                content.getContentId(),
                content.getContentTitle(),
                content.getContentCategory(),
                content.getThumbnailUrl(),
                content.getMainImageUrl(),
                content.getCreatedDateTime()
        );
    }
}

