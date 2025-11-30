package org.appjam.bongbaek.domain.content.repository;

import org.appjam.bongbaek.domain.content.entity.Content;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContentSearchRepository {

    Page<Content> findContentsByCategoryOrderByCreatedDateDesc(Category category, Pageable pageable);
}
