package org.appjam.bongbaek.domain.member.repository;

import java.util.Optional;
import org.appjam.bongbaek.domain.member.entity.Member;
import org.appjam.bongbaek.domain.member.entity.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByOauthIdAndOauthProvider(String oauthId, OAuthProvider oauthProvider);
    Optional<Member> findByOauthIdAndOauthProvider(String oauthId, OAuthProvider oauthProvider);
}
