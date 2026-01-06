package org.appjam.bongbaek.domain.content.dto.response;

import org.appjam.bongbaek.domain.content.entity.Content;

import java.util.List;

public record ContentHomeResponseDto(
        List<ContentResponseDto> contents
        ) {
        public static ContentHomeResponseDto from(List<Content> contents) {
            return new ContentHomeResponseDto(
                    contents.stream()
                            .map(ContentResponseDto::from)
                            .toList());
        }

        private record ContentResponseDto(
            String contentId,
            String contentTitle,
            String contentCategory,
            String thumbnailUrl
        ) {
            public static ContentResponseDto from(Content content) {

                return new ContentResponseDto(
                        content.getContentId(),
                        content.getContentTitle(),
                        content.getContentCategory().getDescription(),
                        content.getThumbnailUrl()
                );
            }
        }
}
