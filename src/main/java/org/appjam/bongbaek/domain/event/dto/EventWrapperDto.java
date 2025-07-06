package org.appjam.bongbaek.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class EventWrapperDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EventDetailResponseDto event;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<EventHomeResponseDto> events;

    public static EventWrapperDto of(EventDetailResponseDto data) {
        return EventWrapperDto.builder()
                .event(data)
                .build();
    }

    public static EventWrapperDto from(List<EventHomeResponseDto> data) {
        return EventWrapperDto.builder()
                .events(data)
                .build();
    }
}
