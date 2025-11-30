package org.appjam.bongbaek.domain.event.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record EventSearchRequestDto(
		int year,
		@Min(1) @Max(12)
		int month,
		String category,
		Boolean attended
) {
}
