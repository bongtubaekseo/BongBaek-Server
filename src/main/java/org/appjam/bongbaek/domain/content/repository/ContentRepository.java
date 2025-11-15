package org.appjam.bongbaek.domain.content.repository;

import org.appjam.bongbaek.domain.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, String> {

    @Query("SELECT c FROM Content c LEFT JOIN FETCH c.contentImage WHERE c.contentId = :contentId")
    Optional<Content> findByIdWithContentImage(String contentId);
}
