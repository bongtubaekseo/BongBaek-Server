package org.appjam.bongbaek.domain.event.dto.response;

import static org.appjam.bongbaek.domain.event.dto.EventCommonDto.*;
import org.appjam.bongbaek.domain.event.entity.Event;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class EventHomeResponseDto {

    private HostInfo hostInfo;
    private EventInfo eventInfo;
    private LocationInfo locationInfo;

    public static EventHomeResponseDto of(Event event) {

        HostInfo hostInfo = HostInfo.builder()
                .hostName(event.getHostName())
                .hostNickname(event.getHostNickname())
                .build();

        EventInfo eventInfo = EventInfo.builder()
                .eventId(event.getEventId().toString())
                .eventCategory(event.getEventCategory())
                .eventRelationship(event.getRelationship())
                .cost(event.getCost())
                .isAttend(null)                  // null 입력 시 JSON에서 제외
                .eventDate(event.getEventDate())
                .note(null)
                .build();

        LocationInfo locationInfo = LocationInfo.builder()
                .location(event.getLocation())
                .address(null)
                .longitude(null)
                .latitude(null)
                .build();

        return EventHomeResponseDto.builder()
                .hostInfo(hostInfo)
                .eventInfo(eventInfo)
                .locationInfo(locationInfo)
                .build();
    }
}
