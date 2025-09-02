package org.appjam.bongbaek.domain.member.dto.response;

import java.time.LocalDate;
import org.appjam.bongbaek.domain.member.entity.IncomeType;

public record MyInfoResponse(
    String memberName,
    LocalDate memberBirthday,
    IncomeType memberIncome
) {
}