package org.appjam.bongbaek.domain.member.repository;

import org.appjam.bongbaek.domain.member.entity.MemberToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {

    MemberToken findByMemberId(final UUID memberId);
    boolean existsByMemberId(final UUID memberId);
    void deleteByMemberId(final UUID memberId);

}