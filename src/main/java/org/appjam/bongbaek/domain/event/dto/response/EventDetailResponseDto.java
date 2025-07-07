package org.appjam.bongbaek.domain.event.dto.response;

import org.appjam.bongbaek.domain.event.dto.common.*;
import org.appjam.bongbaek.domain.event.entity.Event;

public record EventDetailResponseDto(
        HostInfo hostInfo,
        EventInfo eventInfo,
        LocationInfo locationInfo
) {
    public static EventDetailResponseDto of(Event event) {
        return new EventDetailResponseDto(
                new HostInfo(
                        event.getHostName(),
                        event.getHostNickname()),
                new EventInfo(
                        event.getEventCategory().getDescription(),
                        event.getRelationship().getDescription(),
                        event.getCost(),
                        event.isAttended(),
                        event.getEventDate(),
                        event.getNote()
                ),
                new LocationInfo(
                        event.getLocation(),
                        event.getAddress(),
                        event.getLatitude(),
                        event.getLongitude()
                )
        );
    }
}
