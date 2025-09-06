package org.appjam.bongbaek.domain.member.enums;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "회원 탈퇴 사유")
public enum WithdrawalReason {
    INCONVENIENT("이용이 불편함"),
    PRIVACY_CONCERN("개인정보/보안 우려"),
    RARELY_USED("이용 빈도가 낮음"),
    BUG_OR_ERROR("버그/오류 발생"),
    NEW_ACCOUNT("새 계정 생성"),
    OTHER("기타(상세 사유 입력 필수)");

    private final String description;
}
