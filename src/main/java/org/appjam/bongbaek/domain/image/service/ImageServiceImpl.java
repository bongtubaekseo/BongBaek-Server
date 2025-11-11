package org.appjam.bongbaek.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.domain.image.dto.ImageResponseDto;
import org.appjam.bongbaek.domain.image.entity.Image;
import org.appjam.bongbaek.domain.image.repository.ImageRepository;
import org.appjam.bongbaek.domain.image.dto.ImageUploadRequestDto;
import org.appjam.bongbaek.global.exception.image.ImageNotFoundException;
import org.appjam.bongbaek.global.s3.dto.FileDto;
import org.appjam.bongbaek.global.s3.uploader.FileUploader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final FileUploader fileUploader;
    private final ImageRepository imageRepository;


    @Override
    public void uploadImage(
            ImageUploadRequestDto requestDto
    ) {
        FileDto fileDto = fileUploader.upload(requestDto.file(), requestDto.ownerType());

        Image image = Image.builder()
                .imageUrl(fileDto.imageUrl())
                .storageKey(fileDto.storageKey())
                .ownerType(requestDto.ownerType())
                .ownerId(requestDto.ownerId())
                .build();

        imageRepository.save(image);
    }

    @Override
    public void deleteImage(
            String imageId
    ) {
        Image image = imageRepository.findById(imageId)
                        .orElseThrow(ImageNotFoundException::new);

        fileUploader.delete(image.getStorageKey());
        imageRepository.delete(image);
    }

    @Override
    public ImageResponseDto findImageByImageId(
            String imageId
    ) {
        return ImageResponseDto.from(imageRepository.findImageByImageId(imageId));
    }

    @Override
    public List<ImageResponseDto> findImagesByOwnerIdOrderBySequence(
            String ownerId
    ) {
        return ImageResponseDto.fromList(imageRepository.findImagesByOwnerIdOrderBySequenceAsc(ownerId));
    }
}
