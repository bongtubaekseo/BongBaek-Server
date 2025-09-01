package org.appjam.bongbaek.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateMemberRequest(
    @NotBlank(message = "이름은 필수입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$", message = "특수문자는 기입할 수 없어요")
    @Size(max = 30, message = "이름은 최대 30자까지 입력 가능합니다.")
    @Schema(description = "회원 이름", example = "김민경")
    String memberName,

    @NotNull(message = "생일은 필수입니다.")
    @Past(message = "생일은 오늘보다 과거 날짜여야 합니다.")
    @Schema(description = "회원 생일", example = "2001-02-18")
    LocalDate memberBirthday,

    @NotBlank(message = "소득은 필수입니다.")
    @Schema(description = "회원 소득", example = "200만원 이상")
    String memberIncome
) {}