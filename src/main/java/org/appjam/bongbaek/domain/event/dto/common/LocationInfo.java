package org.appjam.bongbaek.domain.event.test.dto.common;

import org.appjam.bongbaek.domain.event.entity.Event;
import org.hibernate.validator.constraints.Range;

public record LocationInfo(
		String location,
		String address,
		@Range(min = -90, max = 90, message = "위도값의 범위는 -90이상 90이하입니다")
		double latitude,
		@Range(min = -180, max = 180, message = "경도값의 범위는 -180이상 180이하입니다")
		double longitude
) {
	public static LocationInfo from(Event event) {
		return new LocationInfo(event.getLocation(), event.getAddress(), event.getLatitude(), event.getLongitude());
	}
}
