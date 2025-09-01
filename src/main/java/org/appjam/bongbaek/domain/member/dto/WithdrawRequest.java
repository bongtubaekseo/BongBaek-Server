package org.appjam.bongbaek.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import org.appjam.bongbaek.domain.member.enums.WithdrawalReason;

public record WithdrawRequest (
    @Schema(description = "탈퇴 사유 (없으면 null)", example = "INCONVENIENT")
    WithdrawalReason withdrawalReason,

    @Schema(description = "자유 입력 사유 (reason이 null일 때 필수, 영문/한글/특수문자/공백 포함 최대 50자)")
    @Size(max = 50, message = "상세 사유는 50자 이내로 입력해주세요.")
    String detail
) {
    // 검증 단계
    @AssertTrue(message = "사전 정의 사유를 선택하거나 상세 사유를 1~50자 입력해야 합니다.")
    @Schema(hidden = true)
    public boolean isValid() {
        if (withdrawalReason == null) {
            return detail != null && !detail.isBlank() && detail.trim().length() <= 50;
        }
        return true;
    }
}