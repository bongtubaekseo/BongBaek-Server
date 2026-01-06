package org.appjam.bongbaek.global.s3.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class S3ClientHelper {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public void upload(
            String key,
            String contentType,
            long contentLength,
            InputStream inputStream
    ) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .contentLength(contentLength)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
    }

    public void delete(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    public void deleteDirectory(String prefix) {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(prefix)
                .build();

        ListObjectsV2Iterable listObjectsV2Iterable = s3Client.listObjectsV2Paginator(listObjectsV2Request);

        List<ObjectIdentifier> objectIdentifiers = new ArrayList<>();
        listObjectsV2Iterable.stream()
                .flatMap(response -> response.contents().stream())
                .forEach(s3Object -> objectIdentifiers.add(ObjectIdentifier.builder().key(s3Object.key()).build()));

        if (!objectIdentifiers.isEmpty()) {
            DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                    .bucket(bucket)
                    .delete(Delete.builder().objects(objectIdentifiers).build())
                    .build();
            s3Client.deleteObjects(deleteObjectsRequest);
        }
    }

    public String getUrl(String key) {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }
}
