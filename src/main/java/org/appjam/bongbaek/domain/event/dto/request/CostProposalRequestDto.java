package org.appjam.bongbaek.domain.event.dto.request;

import org.appjam.bongbaek.domain.event.dto.common.HighAccuracy;
import org.appjam.bongbaek.domain.event.dto.common.LocationInfo;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record CostProposalRequestDto(
		String category,
		String relationship,
		boolean attended,
		LocationInfo locationInfo,
		@Valid HighAccuracy highAccuracy
		) {
}
