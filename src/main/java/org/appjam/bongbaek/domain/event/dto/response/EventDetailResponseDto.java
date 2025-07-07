package org.appjam.bongbaek.domain.event.dto.response;

import static org.appjam.bongbaek.domain.event.dto.EventCommonDto.*;
import org.appjam.bongbaek.domain.event.entity.Event;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class EventDetailResponseDto {

    private HostInfo hostInfo;
    private EventInfo eventInfo;
    private LocationInfo locationInfo;

    public static EventDetailResponseDto of(Event event) {

        HostInfo hostInfo = HostInfo.builder()
                .hostName(event.getHostName())
                .hostNickname(event.getHostNickname())
                .build();

        EventInfo eventInfo = EventInfo.builder()
                .eventId(event.getEventId().toString())
                .eventCategory(event.getEventCategory())
                .eventRelationship(event.getRelationship())
                .cost(event.getCost())
                .isAttend(event.isAttended())
                .eventDate(event.getEventDate())
                .note(event.getNote())
                .build();

        LocationInfo locationInfo = LocationInfo.builder()
                .location(event.getLocation())
                .address(event.getAddress())
                .longitude(event.getLongitude())
                .latitude(event.getLatitude())
                .build();

        return EventDetailResponseDto.builder()
                .hostInfo(hostInfo)
                .eventInfo(eventInfo)
                .locationInfo(locationInfo)
                .build();
    }
}
