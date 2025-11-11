package org.appjam.bongbaek.global.s3.uploader;

import io.hypersistence.tsid.TSID;
import org.appjam.bongbaek.domain.image.entity.OwnerType;
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

@Component
public class S3Uploader implements FileUploader {
    private S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public FileDto upload(
            MultipartFile file,
            OwnerType ownerType
    ) {

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String s3Key = ownerType + "/" + TSID.from(13) + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        String imageUrl = s3Client.utilities().getUrl(getUrlRequest).toString();

        return FileDto.of(s3Key, imageUrl);
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
}
