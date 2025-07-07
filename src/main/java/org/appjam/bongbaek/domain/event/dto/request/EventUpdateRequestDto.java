package org.appjam.bongbaek.domain.event.dto.request;

import static org.appjam.bongbaek.domain.event.dto.EventCommonDto.*;

public record EventUpdateRequestDto(
        HostInfo hostInfo,
        EventInfo eventInfo,
        LocationInfo locationInfo
) {
}
