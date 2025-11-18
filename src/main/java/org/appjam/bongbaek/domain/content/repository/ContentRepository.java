package org.appjam.bongbaek.domain.content.repository;

import org.appjam.bongbaek.domain.content.entity.Content;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, String> {
    List<Content> findTop3ByOrderByCreatedDateTimeDesc();

    Page<Content> findAllByOrderByCreatedDateTimeDesc(Pageable pageable);

    Page<Content> findContentsByContentCategoryOrderByCreatedDateTimeDesc(Category category, Pageable pageable);
}
