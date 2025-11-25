package org.appjam.bongbaek.domain.image.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OwnerType {
    CONTENT("content");

    private final String type;

    @Override
    public String toString() {
        return this.type;
    }
}
