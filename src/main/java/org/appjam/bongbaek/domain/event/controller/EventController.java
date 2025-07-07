package org.appjam.bongbaek.domain.event.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.appjam.bongbaek.domain.event.dto.request.EventDeleteRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventUpdateRequestDto;
import org.appjam.bongbaek.domain.event.dto.response.EventDetailResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventHomeResponseDto;
import org.appjam.bongbaek.domain.event.service.EventService;
import org.appjam.bongbaek.global.api.ApiResponse;
import org.appjam.bongbaek.global.common.CommonSuccessCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<EventWrapperDto>> getEventByEventId(
            @PathVariable String eventId,   // NOTE: 클라 요청 간에는 무조건 String
            String memberId    // TO DO: 멤버 JWT 구현 시 Refactor (현재 25-07-07 사용 X)
    ){

        UUID eventUUID = UUID.fromString(eventId);
        UUID memberUUID = UUID.fromString(memberId);

        EventDetailResponseDto event = eventService.getEventByEventId(eventUUID, memberUUID);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.GET_EVENT, EventWrapperDto.of(event)));
    }

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<EventWrapperDto>> getEventsForHome(
            String memberId
    ){

        UUID memberUUId =  UUID.fromString(memberId);
        List<EventHomeResponseDto> events = eventService.getEventsForHome(LocalDate.now(), memberUUId);

        if (events.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success(CommonSuccessCode.NO_EVENT, EventWrapperDto.from(events)));

        }
        else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success(CommonSuccessCode.GET_EVENT, EventWrapperDto.from(events)));
        }
    }

    @PutMapping("{eventId}")
    public ResponseEntity<ApiResponse<Void>> updateEvent(
            @PathVariable String eventId,
            String memberId,
            @RequestBody EventUpdateRequestDto request
    ){
        UUID eventUUID = UUID.fromString(eventId);
        UUID memberUUID = UUID.fromString(memberId);

        eventService.updateEventByEventId(eventUUID, memberUUID, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.UPDATED_EVENT));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEventByEventId(
            @PathVariable String eventId,
            String memberId
    ) {

        UUID eventUUID = UUID.fromString(eventId);
        UUID memberUUID = UUID.fromString(memberId);

        eventService.deleteEventByEventId(eventUUID, memberUUID);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.DELETED_EVENT));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteEvents(
            String memberId,
            @RequestBody EventDeleteRequestDto request
    ){

        UUID memberUUID = UUID.fromString(memberId);
        List<UUID> eventList = request.eventIds().stream()
                .map(UUID::fromString)
                .toList();

        eventService.deleteEvents(eventList, memberUUID);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.DELETED_EVENT));
    }
}
