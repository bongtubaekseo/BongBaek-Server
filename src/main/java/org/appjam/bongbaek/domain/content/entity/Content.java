package org.appjam.bongbaek.domain.content.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import org.appjam.bongbaek.domain.common.BaseEntity;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "content")
@Comment("경조사 컨텐츠 정보")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends BaseEntity {
    @Id
    @Tsid
    @Column(name = "content_id", length = 13)
    private String contentId;

    @Column(name = "content_title", nullable = false)
    private String contentTitle;

    @Column(name = "content_category", nullable = false)
    private Category contentCategory;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ContentImage> contentImages = new ArrayList<>();

    @Builder
    public Content(String contentTitle, Category contentCategory, String thumbnailUrl) {
        this.contentTitle = contentTitle;
        this.contentCategory = contentCategory;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void updateThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void addContentImage(ContentImage contentImage) {
        this.contentImages.add(contentImage);

        contentImage.setContent(this);
    }

    public Optional<ContentImage> getThumbnail() {
        return this.contentImages
                .stream()
                .filter(ContentImage::isThumbnail)
                .findFirst();
    }
}
