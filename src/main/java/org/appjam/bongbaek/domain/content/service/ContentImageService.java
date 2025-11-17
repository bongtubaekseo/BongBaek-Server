package org.appjam.bongbaek.domain.content.service;

import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.domain.image.entity.OwnerType;
import org.appjam.bongbaek.global.exception.image.InvalidImageRequestException;
import org.appjam.bongbaek.global.s3.dto.FileDto;
import org.appjam.bongbaek.global.s3.uploader.FileUploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ContentImageService {

    private final FileUploader fileUploader;

    public FileDto uploadImage(MultipartFile file) {
        if(file == null || file.isEmpty()){
            throw new InvalidImageRequestException();
        }

        return fileUploader.upload(file, OwnerType.CONTENT);
    }

    public void deleteImage(String storageKey) {
        fileUploader.delete(storageKey);
    }
}
