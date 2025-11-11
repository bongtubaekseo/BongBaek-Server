package org.appjam.bongbaek.domain.image.repository;

import org.appjam.bongbaek.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findImagesByOwnerIdOrderBySequenceAsc(String ownerId);

    Image findImageByImageId(String imageId);
}
