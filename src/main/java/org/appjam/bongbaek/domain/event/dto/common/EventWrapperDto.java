package org.appjam.bongbaek.domain.event.dto.common;

import java.util.List;

import org.appjam.bongbaek.domain.event.dto.response.EventDetailResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventHomeResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class EventWrapperDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EventDetailResponseDto event;       // 상세 조회용

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<EventHomeResponseDto> events;  // 홈, 전체 조회용

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
