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
                HostInfo.from(event),
                EventInfo.from(event),
                LocationInfo.from(event)
        );
    }
}
