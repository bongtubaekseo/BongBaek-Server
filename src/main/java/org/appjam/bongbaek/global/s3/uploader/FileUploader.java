package org.appjam.bongbaek.global.s3.uploader;

import org.appjam.bongbaek.domain.image.entity.OwnerType;
import org.appjam.bongbaek.global.s3.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    FileDto upload(MultipartFile file, OwnerType ownerType);

    void delete(String storageId);
}
