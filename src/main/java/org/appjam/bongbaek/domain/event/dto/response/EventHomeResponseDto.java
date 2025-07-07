package org.appjam.bongbaek.domain.event.dto.response;

import org.appjam.bongbaek.domain.event.dto.common.EventInfo;
import org.appjam.bongbaek.domain.event.dto.common.HostInfo;
import org.appjam.bongbaek.domain.event.dto.common.LocationInfo;
import org.appjam.bongbaek.domain.event.entity.Event;

public record EventHomeResponseDto (
        String eventId,
        HostInfo hostInfo,
        EventInfo eventInfo,
        LocationInfo locationInfo
){
    public static EventHomeResponseDto of(Event event) {
        return new EventHomeResponseDto(
                event.getEventId().toString(),
                new HostInfo(
                        event.getHostName(),
                        event.getHostNickname()
                ),
                new EventInfo(
                        event.getEventCategory().getDescription(),
                        event.getRelationship().getDescription(),
                        event.getCost(),
                        null,
                        event.getEventDate()
                ),
                new LocationInfo(
                        event.getLocation(),
                        null,
                        null,
                        null
                )
        );
    }
}
