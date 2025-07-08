package org.appjam.bongbaek.domain.event.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.appjam.bongbaek.domain.event.code.EventSuccessCode;
import org.appjam.bongbaek.domain.event.dto.common.EventWrapperDto;
import org.appjam.bongbaek.domain.event.dto.request.EventDeleteRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventUpdateRequestDto;
import org.appjam.bongbaek.domain.event.dto.response.EventDetailResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventHomeResponseDto;
import org.appjam.bongbaek.domain.event.exception.InvalidUUIDFormatException;
import org.appjam.bongbaek.domain.event.service.EventService;
import org.appjam.bongbaek.global.api.ApiResponse;
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
            @RequestHeader String memberId    // TO DO: 멤버 JWT 구현 시 Refactor (현재 25-07-07 사용 X)
    ){

        UUID eventUUID = parseUUID(eventId);
        UUID memberUUID = parseUUID(memberId);

        EventDetailResponseDto event = eventService.getEventByEventId(eventUUID, memberUUID);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.GET_EVENT, EventWrapperDto.of(event)));
    }

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<EventWrapperDto>> getEventsForHome(
            @RequestHeader String memberId
    ){
        UUID memberUUId =  parseUUID(memberId);
        List<EventHomeResponseDto> events = eventService.getEventsForHome(LocalDate.now(), memberUUId);

        if (events.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success(EventSuccessCode.NO_EVENT, EventWrapperDto.from(events)));

        }
        else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success(EventSuccessCode.GET_EVENT, EventWrapperDto.from(events)));
        }
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> updateEvent(
            @PathVariable String eventId,
            @RequestHeader String memberId,
            @RequestBody EventUpdateRequestDto request
    ){
        UUID eventUUID = parseUUID(eventId);
        UUID memberUUID = parseUUID(memberId);

        eventService.updateEventByEventId(eventUUID, memberUUID, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.UPDATED_EVENT));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEventByEventId(
            @PathVariable String eventId,
            @RequestHeader String memberId
    ) {

        UUID eventUUID = parseUUID(eventId);
        UUID memberUUID = parseUUID(memberId);

        eventService.deleteEventByEventId(eventUUID, memberUUID);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.DELETED_EVENT));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteEvents(
            @RequestHeader String memberId,
            @RequestBody EventDeleteRequestDto request
    ){

        UUID memberUUID = parseUUID(memberId);
        List<UUID> eventList = request.eventIds().stream()
                .map(UUID::fromString)
                .toList();

        eventService.deleteEvents(eventList, memberUUID);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.DELETED_EVENT));
    }

    private UUID parseUUID(String id) {

        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDFormatException();
        }
    }
}
