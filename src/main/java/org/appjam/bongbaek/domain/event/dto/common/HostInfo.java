package org.appjam.bongbaek.domain.event.dto.common;

import org.appjam.bongbaek.domain.event.entity.Event;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HostInfo(
		@Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$", message = "특수문자는 기입할 수 없어요")
		@Size(min = 2, max = 10, message = "2자 이상 10자 이내만 기입할 수 있어요")
		String hostName,
		@Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$", message = "특수문자는 기입할 수 없어요")
		@Size(min = 2, max = 10, message = "2자 이상 10자 이내만 기입할 수 있어요")
		String hostNickname) {

	public static HostInfo from(Event event) {
		return new HostInfo(event.getHostName(), event.getHostNickname());
	}
}
