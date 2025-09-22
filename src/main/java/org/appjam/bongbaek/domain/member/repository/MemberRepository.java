package org.appjam.bongbaek.domain.member.repository;

import java.util.Optional;
import org.appjam.bongbaek.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByOauthId(final String oauthId);
    Optional<Member> findByOauthIdAndOauthProvider(final String oauthId, final String oauthProvider);
}
