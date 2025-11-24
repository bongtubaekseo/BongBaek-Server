package org.appjam.bongbaek.domain.content.dto.response;

import org.appjam.bongbaek.domain.content.entity.Content;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record ContentListDto(
        List<ContentListElements> contents,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean isLast

) {
    public static ContentListDto of(Page<Content> contents){
        List<ContentListDto.ContentListElements> elements = contents.getContent().stream()
                .map(ContentListElements::from)
                .toList();

        return new ContentListDto(
                elements,
                contents.getNumber(),
                contents.getTotalPages(),
                contents.getTotalElements(),
                contents.isLast());
    }

    private record ContentListElements(
            String contentId,
            String contentTitle,
            String contentCategory,
            String thumbnailUrl,
            String createdAt
    ) {
        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");


        private static ContentListElements from(
                Content content
        ) {
            String createdAt = content.getCreatedDateTime()
                    .format(DATE_FORMATTER);

            return new ContentListElements(
                    content.getContentId(),
                    content.getContentTitle(),
                    content.getContentCategory().getDescription(),
                    content.getThumbnailUrl(),
                    createdAt
                    );
        }
    }
}
