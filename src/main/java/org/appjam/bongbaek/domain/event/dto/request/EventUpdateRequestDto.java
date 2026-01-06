package org.appjam.bongbaek.domain.event.dto.request;

import jakarta.validation.Valid;
import org.appjam.bongbaek.domain.event.dto.common.EventInfo;
import org.appjam.bongbaek.domain.event.dto.common.HostInfo;
import org.appjam.bongbaek.domain.event.dto.common.LocationInfo;

public record EventUpdateRequestDto (
        @Valid HostInfo hostInfo,
        @Valid EventInfo eventInfo,
        @Valid LocationInfo locationInfo
){
}
