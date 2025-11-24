package org.appjam.bongbaek.global.s3.dto;

public record FileDto(
        String storageKey,
        String imageUrl
) {
    public static FileDto of(String storageKey, String imageUrl) {
        return new FileDto(storageKey, imageUrl);
    }
}
