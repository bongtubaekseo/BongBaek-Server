package org.appjam.bongbaek.global.s3.uploader;

import io.hypersistence.tsid.TSID;
import org.appjam.bongbaek.domain.image.entity.OwnerType;
import org.appjam.bongbaek.global.exception.image.FileUploadFailedException;
import org.appjam.bongbaek.global.exception.image.InvalidImageFormatException;
import org.appjam.bongbaek.global.s3.infra.S3ClientHelper;
import org.appjam.bongbaek.global.s3.dto.FileDto;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class S3Uploader implements FileUploader {

    private static final List<String> ALLOWED_EXTENSIONS = List.of(
            ".jpg",
            ".jpeg",
            ".png"
    );

    private final S3ClientHelper s3ClientHelper;

    public FileDto upload(MultipartFile file, OwnerType ownerType, String ownerId){
        String s3Key = createS3Key(file, ownerType, ownerId);

        try {
            s3ClientHelper.upload(
                    s3Key,
                    file.getContentType(),
                    file.getSize(),
                    file.getInputStream()
            );
        } catch (IOException e) {
            throw new FileUploadFailedException();
        }

        return FileDto.of(s3Key, getImageUrl(s3Key));
    }

    public void delete(String s3Key) {
        s3ClientHelper.delete(s3Key);
    }

    @Override
    public void deleteDirectory(OwnerType ownerType, String ownerId) {
        String prefix = ownerType.toString() + "/" + ownerId + "/";

        s3ClientHelper.deleteDirectory(prefix);
    }

    private String createS3Key(MultipartFile file, OwnerType ownerType, String ownerId) {
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new InvalidImageFormatException();
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new InvalidImageFormatException();
        }

        return ownerType + "/" + ownerId + "/" + TSID.fast() + extension;
    }

    private String getImageUrl(String s3Key) {
        return s3ClientHelper.getUrl(s3Key);
    }
}
