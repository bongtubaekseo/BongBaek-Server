package org.appjam.bongbaek.domain.member.repository;

import java.util.Optional;
import org.appjam.bongbaek.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByKakaoId(final Long kakaoId);
    boolean existsByAppleId(final String appleId);
    Optional<Member> findByKakaoId(final Long kakaoId);
    Optional<Member> findByAppleId(final String appleId);
}
