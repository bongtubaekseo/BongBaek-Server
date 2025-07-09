package org.appjam.bongbaek.domain.event.dto.request;

import org.appjam.bongbaek.domain.event.dto.common.HighAccuracy;
import org.appjam.bongbaek.domain.event.dto.common.LocationInfo;

import jakarta.validation.Valid;

public record CostProposalRequestDto(
		String category,
		String relationship,
		boolean attended,
		LocationInfo locationInfo,
		@Valid HighAccuracy highAccuracy
		) {
}
