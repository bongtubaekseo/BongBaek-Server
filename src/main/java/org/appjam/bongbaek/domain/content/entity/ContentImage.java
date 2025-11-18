package org.appjam.bongbaek.domain.content.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import org.appjam.bongbaek.domain.image.entity.BaseImageEntity;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "content_image")
@Comment("경조사 컨텐츠 이미지 정보")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentImage extends BaseImageEntity {
    @Id
    @Tsid
    @Column(name = "content_image_id", length = 13)
    private String imageId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Content content;

    @Setter
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "is_thumbnail", nullable = false)
    private boolean isThumbnail;

    @Builder
    public ContentImage(String imageUrl, String storageKey, Integer sequence, boolean isThumbnail) {
        super(imageUrl, storageKey);
        this.sequence = sequence;
        this.isThumbnail = isThumbnail;
    }
}
