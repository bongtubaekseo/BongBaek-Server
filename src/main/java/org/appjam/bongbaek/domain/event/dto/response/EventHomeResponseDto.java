package org.appjam.bongbaek.domain.event.dto.response;

import org.appjam.bongbaek.domain.event.dto.common.EventInfo;
import org.appjam.bongbaek.domain.event.dto.common.HostInfo;
import org.appjam.bongbaek.domain.event.dto.common.LocationInfo;
import org.appjam.bongbaek.domain.event.entity.Event;

import java.util.UUID;

public record EventHomeResponseDto (
        UUID eventId,
        HostInfo hostInfo,
        EventInfo eventInfo,
        LocationInfo locationInfo
){
    public static EventHomeResponseDto of(Event event, Integer dDay) {
        return new EventHomeResponseDto(
                event.getEventId(),
                HostInfo.from(event),
                EventInfo.from(
                        event.getEventCategory().getDescription(),
                        event.getRelationship().getDescription(),
                        event.getCost(),
                        event.getEventDate(),
                        dDay),
                LocationInfo.from(event)
        );
    }
}
