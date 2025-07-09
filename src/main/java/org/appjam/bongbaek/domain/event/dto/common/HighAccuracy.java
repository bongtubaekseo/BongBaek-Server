package org.appjam.bongbaek.domain.event.dto.common;

import org.hibernate.validator.constraints.Range;

public record HighAccuracy(
		@Range(min = 1, max = 5, message = "1에서 5사이의 값만 입력할 수 있습니다")
		int contactFrequency,
		@Range(min = 1, max = 5, message = "1에서 5사이의 값만 입력할 수 있습니다")
		int meetFrequency
) {
}
