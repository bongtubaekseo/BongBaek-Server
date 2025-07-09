package org.appjam.bongbaek.domain.event.dto.common;

import org.appjam.bongbaek.domain.event.entity.Event;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record LocationInfo(
		String location,
		String address,
		@Range(min = -90, max = 90, message = "위도값의 범위는 -90이상 90이하입니다")
		Double latitude,
		@Range(min = -180, max = 180, message = "경도값의 범위는 -180이상 180이하입니다")
		Double longitude
) {
	public static LocationInfo from(Event event) {
		return new LocationInfo(event.getLocation(), event.getAddress(), event.getLatitude(), event.getLongitude());
	}

	public static LocationInfo from(String location) {
		return new LocationInfo(location, null, null, null);
	}
}
