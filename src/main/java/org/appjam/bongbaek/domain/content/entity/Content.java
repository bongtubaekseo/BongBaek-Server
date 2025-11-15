package org.appjam.bongbaek.domain.content.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.appjam.bongbaek.domain.common.BaseEntity;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.hibernate.annotations.Comment;

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

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Column(name = "thumbnail_storage_key", nullable = false)
    private String thumbnailStorageKey;

    @OneToOne(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private ContentImage contentImage;

    @Builder
    public Content(String contentTitle, Category contentCategory, String thumbnailUrl, String thumbnailStorageKey) {
        this.contentTitle = contentTitle;
        this.contentCategory = contentCategory;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailStorageKey = thumbnailStorageKey;
    }

    public void updateThumbnail(String thumbnailUrl, String thumbnailStorageKey) {
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailStorageKey = thumbnailStorageKey;
    }

    public void uploadContentImage(ContentImage contentImage) {
        this.contentImage = contentImage;

        if (contentImage != null) {
            contentImage.setContent(this);
        }
    }

    public String getMainImageUrl() {
        if (this.contentImage == null) {
            return null;
        }
        return this.contentImage.getImageUrl();
    }
}
