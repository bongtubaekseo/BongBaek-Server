package org.appjam.bongbaek.domain.image.dto;

import org.appjam.bongbaek.domain.image.entity.OwnerType;
import org.springframework.web.multipart.MultipartFile;

public record ImageUploadRequestDto(
        MultipartFile file,
        OwnerType ownerType,
        String ownerId
) {
}
