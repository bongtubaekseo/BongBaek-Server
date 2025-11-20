package org.appjam.bongbaek.domain.content.repository;

import org.appjam.bongbaek.domain.content.entity.Content;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, String> {
    @Query("select a from Content a join fetch a.contentImages where a.contentId = :contentId")
    Optional<Content> findContentByIdWithImages(String contentId);

    List<Content> findTop3ByOrderByCreatedDateTimeDesc();

    Page<Content> findAllByOrderByCreatedDateTimeDesc(Pageable pageable);

    Page<Content> findContentsByContentCategoryOrderByCreatedDateTimeDesc(Category category, Pageable pageable);
}
