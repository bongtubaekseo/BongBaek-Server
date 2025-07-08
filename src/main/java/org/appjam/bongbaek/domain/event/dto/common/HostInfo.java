package org.appjam.bongbaek.domain.event.dto.common;

import org.appjam.bongbaek.domain.event.entity.Event;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HostInfo(
		@Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "이름은 한글이나 영어 대소문자로만 입력 가능합니다")
		@Size(min = 2, max = 10, message = "2자 이상 10자 이내만 기입할 수 있습니다")
		String hostName,
		@Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "별명은 한글이나 영어 대소문자로만 입력 가능합니다")
		@Size(min = 2, max = 10, message = "2자 이상 10자 이내만 기입할 수 있습니다")
		String hostNickname) {

	public static HostInfo from(Event event) {
		return new HostInfo(event.getHostName(), event.getHostNickname());
	}
}
