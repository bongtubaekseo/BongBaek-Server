package org.appjam.bongbaek.domain.member.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.appjam.bongbaek.domain.common.BaseEntity;
import org.appjam.bongbaek.domain.member.dto.WithdrawRequest;
import org.appjam.bongbaek.domain.member.enums.WithdrawalReason;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "member_withdrawal")
@Comment("회원 탈퇴 이력")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberWithdrawal extends BaseEntity {

    @Id
    @Tsid
    @Column(name = "withdrawal_id", length = 13)
    private String withdrawalId;

    @Column(name = "member_id", nullable = false, length = 30)
    private String memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "withdrawal_reason", columnDefinition = "VARCHAR(50)")
    private WithdrawalReason withdrawalReason;

    @Column(name = "detail", length = 150)
    private String detail;

    public MemberWithdrawal(String memberId, WithdrawRequest request) {
        this.memberId = memberId;
        this.withdrawalReason = request.withdrawalReason();

        // reason이 없을 때만 detail 저장
        if (this.withdrawalReason == null) {
            this.detail = (request.detail() == null) ? null : request.detail().trim();
        } else {
            this.detail = null;
        }
    }
}