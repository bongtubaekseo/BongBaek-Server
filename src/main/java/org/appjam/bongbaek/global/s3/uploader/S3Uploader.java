package org.appjam.bongbaek.global.s3.uploader;

import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.domain.image.entity.OwnerType;
import org.appjam.bongbaek.global.exception.image.FileUploadFailedException;
import org.appjam.bongbaek.global.exception.image.InvalidImageFormatException;
import org.appjam.bongbaek.global.s3.dto.FileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

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

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public FileDto upload(
            MultipartFile file,
            OwnerType ownerType
    ) {
        String s3Key = createS3Key(file, ownerType);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new FileUploadFailedException();
        }

        return FileDto.of(s3Key, getImageUrl(s3Key));
    }

    public void delete(
            String s3Key
    ) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    private String createS3Key(MultipartFile file, OwnerType ownerType) {
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new InvalidImageFormatException();
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new InvalidImageFormatException();
        }

        return ownerType + "/" + TSID.fast() + extension;
    }

    private String getImageUrl(String s3Key) {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }
}
