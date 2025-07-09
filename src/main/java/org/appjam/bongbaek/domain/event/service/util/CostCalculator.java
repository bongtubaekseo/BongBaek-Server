package org.appjam.bongbaek.domain.event.service.util;

import org.appjam.bongbaek.domain.event.dto.common.HighAccuracy;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.appjam.bongbaek.domain.event.entity.Relationship;
import org.appjam.bongbaek.domain.event.dto.request.CostProposalRequestDto;
import org.appjam.bongbaek.domain.member.entity.IncomeType;
import org.appjam.bongbaek.domain.member.entity.Member;

public class CostCalculator {
	private static final int UNIT_AMOUNT = 10000;

	public static int calculateCost(Member member, CostProposalRequestDto costProposalRequestDto) {
		// 경조사 종류에 따른 기본금
		Category category = Category.of(costProposalRequestDto.category());
		int defaultCost = category.getDefaultCost();

		// 나이 계수
		float ageCoefficient = calculateAgeCoefficient(member.getAge());

		// 수입 계수
		float incomeCoefficient = calculateIncomeCoefficient(member.getMemberIncome());

		// 경조사 대상과의 관계
		Relationship relationship = Relationship.of(costProposalRequestDto.relationship());
		float relationshipCoefficient = relationship.getScore();

		// 참석 여부
		float attendedCoefficient = calculateAttendedCoefficient(costProposalRequestDto.attended());

		// 만남 빈도, 연락 빈도
		HighAccuracy highAccuracy = costProposalRequestDto.highAccuracy();
		int contactFrequency = highAccuracy.contactFrequency();
		int meetFrequency = highAccuracy.meetFrequency();

		// 금액 산정
		float cost = defaultCost * ageCoefficient * incomeCoefficient * relationshipCoefficient *
				(1 + (attendedCoefficient + meetFrequency + contactFrequency) / 100);

		// 금액 보정을 위한 올림 처리
		int result = (int)Math.ceil(cost / UNIT_AMOUNT);

		// 장례식인 경우 금액을 홀수로 조정
		if (category.equals(Category.FUNERAL) && result % 2 == 0) {
			result = result - 1;
		}

		// 만원 단위로 금액 조정
		return result * UNIT_AMOUNT;
	}

	private static float calculateAgeCoefficient(int age) {
		// 20대 이하면 0.8
		if (age <= 29) {
			return 0.8f;
		}
		// 40대 이상이면 1.2
		if (age >= 40) {
			return 1.2f;
		}
		// 30대면 1
		return 1.0f;
	}

	private static float calculateIncomeCoefficient(IncomeType incomeType) {
		// 수입이 200 이상이면 1.1
		if (incomeType == IncomeType.OVER200) {
			return 1.1f;
		}
		// 수입이 200 이하면 0.8
		if (incomeType == IncomeType.UNDER200) {
			return 0.8f;
		}
		// 수입이 없으면 0.7
		return 0.7f;
	}

	private static float calculateAttendedCoefficient(boolean attended) {
		// 참석인 경우 2
		if (attended) {
			return 2;
		}
		// 불참인 경우 1
		return 1;
	}
}
