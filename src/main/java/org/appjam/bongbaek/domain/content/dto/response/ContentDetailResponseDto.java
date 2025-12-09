package org.appjam.bongbaek.domain.content.dto.response;

import org.appjam.bongbaek.domain.content.entity.Content;
import org.appjam.bongbaek.domain.content.entity.ContentImage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record ContentDetailResponseDto(
        String contentId,
        String contentTitle,
        String contentCategory,
        List<String> imageUrls,
        LocalDate createdAt
) {
    public static ContentDetailResponseDto from(Content content) {

        List<String> mainImageUrls = content.getContentImages().stream()
                .filter(image -> !image.isThumbnail())
                .sorted(Comparator.comparing(ContentImage::getSequence))
                .map(ContentImage::getImageUrl)
                .toList();

        List<String> imageUrls = new ArrayList<>(mainImageUrls);

        return new ContentDetailResponseDto(
                content.getContentId(),
                content.getContentTitle(),
                content.getContentCategory().getDescription(),
                imageUrls,
                content.getCreatedDateTime().toLocalDate()
        );
    }
}
