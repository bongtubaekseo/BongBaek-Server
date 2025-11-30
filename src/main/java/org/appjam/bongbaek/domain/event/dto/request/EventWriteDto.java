package org.appjam.bongbaek.domain.event.dto.request;

import org.appjam.bongbaek.domain.event.entity.Category;
import org.appjam.bongbaek.domain.event.entity.Event;
import org.appjam.bongbaek.domain.event.entity.Relationship;
import org.appjam.bongbaek.domain.event.dto.common.EventInfo;
import org.appjam.bongbaek.domain.event.dto.common.HighAccuracy;
import org.appjam.bongbaek.domain.event.dto.common.HostInfo;
import org.appjam.bongbaek.domain.event.dto.common.LocationInfo;
import org.appjam.bongbaek.domain.member.entity.Member;

import jakarta.validation.Valid;

public record EventWriteDto(
		@Valid HostInfo hostInfo,
		@Valid EventInfo eventInfo,
		@Valid LocationInfo locationInfo,
		@Valid HighAccuracy highAccuracy
) {
	public Event toEntity(Member member) {
		return Event.builder()
				.hostName(hostInfo.hostName())
				.hostNickname(hostInfo.hostNickname())

				.contactFrequency(highAccuracy.contactFrequency())
				.meetFrequency(highAccuracy.meetFrequency())

				.eventCategory(Category.of(eventInfo.eventCategory()))
				.relationship(Relationship.of(eventInfo.relationship()))
				.eventDate(eventInfo.eventDate())
				.cost(eventInfo.cost())
				.note(eventInfo.note())
				.attended(eventInfo.isAttend())

				.location(locationInfo.location())
				.address(locationInfo.address())
				.latitude(locationInfo.latitude())
				.longitude(locationInfo.longitude())
				.member(member)
				.build();
	}
}
