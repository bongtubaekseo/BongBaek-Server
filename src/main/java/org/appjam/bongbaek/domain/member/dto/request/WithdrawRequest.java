package org.appjam.bongbaek.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.appjam.bongbaek.domain.member.entity.MemberWithdrawal;
import org.appjam.bongbaek.domain.member.enums.WithdrawalReason;

public record WithdrawRequest (
    @NotNull(message = "탈퇴 사유는 필수입니다.")
    @Schema(
        description = "탈퇴 사유 (INCONVENIENT/PRIVACY_CONCERN/RARELY_USED/BUG_OR_ERROR/NEW_ACCOUNT/OTHER)",
        example = "OTHER"
    )
    WithdrawalReason withdrawalReason,

    @Schema(
        description = "상세 사유 (OTHER 선택 시 1~50자 필수 입력)",
        example = "서비스가너무좋아서그만탈주"
    )
    @Size(max = 50, message = "상세 사유는 50자 이내로 입력해주세요.")
    String detail
) {
    public MemberWithdrawal toEntity() {
        return MemberWithdrawal.builder()
            .withdrawalReason(withdrawalReason)
            .detail(detail)
            .build();
    }
}