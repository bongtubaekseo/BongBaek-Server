package org.appjam.bongbaek.domain.member.repository;

import org.appjam.bongbaek.domain.member.entity.MemberWithdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberWithdrawalRepository extends JpaRepository<MemberWithdrawal, String> {
}