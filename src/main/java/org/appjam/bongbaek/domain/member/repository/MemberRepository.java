package org.appjam.bongbaek.domain.member.repository;

import org.appjam.bongbaek.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    boolean existsBykakaoId(final Long kakaoId);
    Member findBykakaoId(final Long kakaoId);
    Optional<Member> findById(UUID memberId);
}
