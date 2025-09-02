package org.appjam.bongbaek.domain.member.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.appjam.bongbaek.domain.common.BaseEntity;
import org.appjam.bongbaek.domain.member.enums.WithdrawalReason;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "withdrawal_reason", columnDefinition = "VARCHAR(50)")
    private WithdrawalReason withdrawalReason;

    @Column(name = "detail", length = 150)
    private String detail;

    @Builder
    private MemberWithdrawal(WithdrawalReason withdrawalReason, String detail) {
        this.withdrawalReason = withdrawalReason;
        this.detail = validateWithdrawalDetail(withdrawalReason, detail);
    }

    /**
     * 탈퇴 사유와 상세 사유를 검증
     */
    private String validateWithdrawalDetail(WithdrawalReason reason, String detail) {
        if (reason == WithdrawalReason.OTHER) {
            String trimmed = detail == null ? null : detail.trim();
            if (trimmed == null || trimmed.isBlank() || trimmed.length() > 50) {
                throw new CustomException(CommonErrorCode.INVALID_WITHDRAWAL_DETAIL);
            }
            return trimmed;
        }

        if (detail != null) {
            throw new CustomException(CommonErrorCode.WITHDRAWAL_DETAIL_NOT_ALLOWED);
        }

        return null;
    }
}