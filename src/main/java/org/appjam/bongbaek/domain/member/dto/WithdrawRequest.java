package org.appjam.bongbaek.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.appjam.bongbaek.domain.member.enums.WithdrawalReason;

public record WithdrawRequest (
    @NotNull(message = "탈퇴 사유는 필수입니다.")
    @Schema(
        description = "탈퇴 사유 (INCONVENIENT/PRIVACY_CONCERN/RARELY_USED/BUG_OR_ERROR/NEW_ACCOUNT/OTHER)",
        example = "INCONVENIENT"
    )
    WithdrawalReason withdrawalReason,

    @Schema(
        description = "상세 사유 (OTHER 선택 시 1~50자 필수 입력)",
        example = "서비스가너무좋아서그만탈주"
    )
    @Size(max = 50, message = "상세 사유는 50자 이내로 입력해주세요.")
    String detail
) {
    // 검증 단계
    @AssertTrue(message = "사유가 OTHER인 경우 상세 사유를 1~50자로 입력해야 합니다. OTHER가 아니면 상세 사유를 null로 보내야 합니다.")
    @Schema(hidden = true)
    public boolean isDetailValidWhenOther() {
        if (withdrawalReason == WithdrawalReason.OTHER) {
            // OTHER → detail 필수 (1~50자)
            return detail != null && !detail.isBlank() && detail.trim().length() <= 50;
        } else {
            // OTHER가 아닐 때 → detail 반드시 null
            return detail == null;
        }
    }
}