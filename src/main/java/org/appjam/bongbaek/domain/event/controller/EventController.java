package org.appjam.bongbaek.domain.event.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.appjam.bongbaek.domain.event.code.EventSuccessCode;
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
    public ResponseEntity<ApiResponse<EventDetailResponseDto>> getEventByEventId(
            @PathVariable String eventId,   // NOTE: 클라 요청 간에는 무조건 String
            @RequestHeader String memberId    // TO DO: 멤버 JWT 구현 시 Refactor (현재 25-07-07 사용 X)
    ){

        UUID eventUUID = UUID.fromString(eventId);
        UUID memberUUID = UUID.fromString(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.GET_EVENT, eventService.getEventByEventId(eventUUID, memberUUID)));
    }

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<EventHomeResponseDto>> getEventsForHome(
            @RequestHeader String memberId
    ){

        UUID memberUUID = UUID.fromString(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.GET_EVENT, eventService.getEventsForHome(LocalDate.now(), memberUUID)));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> updateEvent(
            @PathVariable String eventId,
            @RequestHeader String memberId,
            @RequestBody @Valid EventUpdateRequestDto request
    ){
        UUID eventUUID = UUID.fromString(eventId);
        UUID memberUUID = UUID.fromString(memberId);

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

        UUID eventUUID = UUID.fromString(eventId);
        UUID memberUUID = UUID.fromString(memberId);

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

        UUID memberUUID = UUID.fromString(memberId);
        List<UUID> eventList = request.eventIds().stream()
                .map(UUID::fromString)
                .toList();

        eventService.deleteEvents(eventList, memberUUID);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.DELETED_EVENT));
    }
}
