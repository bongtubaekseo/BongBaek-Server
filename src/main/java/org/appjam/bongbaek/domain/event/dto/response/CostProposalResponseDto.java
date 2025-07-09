package org.appjam.bongbaek.domain.event.dto.response;

import org.appjam.bongbaek.domain.event.service.util.vo.CostParamInfo;
import org.appjam.bongbaek.domain.event.service.util.vo.RangeInfo;

public record CostProposalResponseDto(
		int cost,
		CostRange range,
		String category,
		String location,
		ProposalParams params
) {
	public static CostProposalResponseDto of(
			int cost,
			RangeInfo rangeInfo,
			String category,
			String location,
			CostParamInfo costParams
	) {
		return new CostProposalResponseDto(
				cost,
				CostRange.of(rangeInfo),
				category,
				location,
				ProposalParams.of(costParams));
	}

	private record CostRange(int min, int max) {
		private static CostRange of(RangeInfo rangeInfo) {
			return new CostRange(rangeInfo.min(), rangeInfo.max());
		}
	}

	private record ProposalParams(
			int age,
			String income,
			String category,
			String relationship,
			boolean attended,
			int contactFrequency,
			int meetFrequency
	) {
		private static ProposalParams of(CostParamInfo costParams) {
			return new ProposalParams(
					costParams.age(),
					costParams.income(),
					costParams.category(),
					costParams.relationship(),
					costParams.attended(),
					costParams.contactFrequency(),
					costParams.meetFrequency());
		}
	}
}
