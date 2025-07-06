package org.appjam.bongbaek.domain.event.controller;

import java.util.List;
import java.util.UUID;

import org.appjam.bongbaek.domain.event.code.EventSuccessCode;
import org.appjam.bongbaek.domain.event.dto.EventDetailResponseDto;
import org.appjam.bongbaek.domain.event.dto.EventHomeResponseDto;
import org.appjam.bongbaek.domain.event.dto.EventWrapperDto;
import org.appjam.bongbaek.domain.event.repository.EventRepository;
import org.appjam.bongbaek.domain.event.service.EventService;
import org.appjam.bongbaek.domain.member.entity.Member;
import org.appjam.bongbaek.global.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventRepository eventRepository;
    private final EventService eventService;

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventWrapperDto>> getEventByEventId(
            @PathVariable String eventId
            ){
        UUID targetId = UUID.fromString(eventId);

        EventDetailResponseDto event = eventService.getEventByEventId(targetId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.OK, EventWrapperDto.of(event)));
    }

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<EventWrapperDto>> getEventsForHome(
            Member member   // 멤버 JWT 구현 시 리팩터링 (현재 25-07-07 사용 X)
    ){

        List<EventHomeResponseDto> events = eventService.getEventsForHome(member);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.OK, EventWrapperDto.from(events)));
    }
}
