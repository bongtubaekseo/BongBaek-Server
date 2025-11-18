package org.appjam.bongbaek.domain.content.dto.request;

import jakarta.validation.constraints.NotNull;

public record ContentWriteDto(
        @NotNull
        String contentTitle,
        @NotNull
        String contentCategory
) {
}

