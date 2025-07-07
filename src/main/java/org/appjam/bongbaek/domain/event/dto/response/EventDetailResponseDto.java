package org.appjam.bongbaek.domain.event.dto.response;

import org.appjam.bongbaek.domain.event.dto.common.*;
import org.appjam.bongbaek.domain.event.entity.Event;

public record EventDetailResponseDto(
        String eventId,
        HostInfo hostInfo,
        EventInfo eventInfo,
        LocationInfo locationInfo
) {
    public static EventDetailResponseDto of(Event event) {
        return new EventDetailResponseDto(
                event.getEventId().toString(),
                new HostInfo(
                        event.getHostName(),
                        event.getHostNickname()
                ),
                new EventInfo(
                        event.getEventCategory().getDescription(),
                        event.getRelationship().getDescription(),
                        event.getCost(),
                        event.isAttended(),
                        event.getEventDate()
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
