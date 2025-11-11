package org.appjam.bongbaek.domain.image.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Image {

    @Id
    @Tsid
    @Column(name = "image_id", length = 13)
    private String imageId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "storage_key", nullable = false)
    private String storageKey;

    @Column(name = "owner_type", nullable = false)
    private OwnerType ownerType;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Builder
    public Image(String imageUrl, String storageKey, OwnerType ownerType, String ownerId) {
        this.imageUrl = imageUrl;
        this.storageKey = storageKey;
        this.ownerType = ownerType;
        this.ownerId = ownerId;
    }
}
