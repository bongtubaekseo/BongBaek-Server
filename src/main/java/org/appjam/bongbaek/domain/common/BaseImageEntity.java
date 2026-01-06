package org.appjam.bongbaek.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseImageEntity {
    @Column(name = "storage_key", nullable = false)
    private String storageKey;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    protected BaseImageEntity(String imageUrl, String storageKey) {
        this.imageUrl = imageUrl;
        this.storageKey = storageKey;
    }
}
