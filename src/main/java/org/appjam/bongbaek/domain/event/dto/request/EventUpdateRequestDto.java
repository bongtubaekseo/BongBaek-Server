package org.appjam.bongbaek.domain.event.dto.request;

import org.appjam.bongbaek.domain.event.dto.common.*;

public record EventUpdateRequestDto (
        HostInfo hostInfo,
        EventInfo eventInfo,
        LocationInfo locationInfo
){
}
