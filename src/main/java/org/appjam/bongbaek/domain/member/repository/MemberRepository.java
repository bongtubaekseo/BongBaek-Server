package org.appjam.bongbaek.domain.member.repository;

import java.util.UUID;

import org.appjam.bongbaek.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, UUID> {
}
