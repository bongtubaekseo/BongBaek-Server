package org.appjam.bongbaek.domain.event.service.util.vo;

import org.appjam.bongbaek.domain.event.dto.request.CostProposalRequestDto;
import org.appjam.bongbaek.domain.member.entity.Member;

public record CostParamInfo(
		int age,
		String income,
		String category,
		String relationship,
		boolean attended,
		int contactFrequency,
		int meetFrequency
) {
	public static CostParamInfo of(Member member, CostProposalRequestDto costProposalRequestDto) {
		return new CostParamInfo(
				member.getAge(),
				member.getMemberIncome().getDescription(),
				costProposalRequestDto.category(),
				costProposalRequestDto.relationship(),
				costProposalRequestDto.attended(),
				costProposalRequestDto.highAccuracy().contactFrequency(),
				costProposalRequestDto.highAccuracy().meetFrequency()
		);
	}
}
