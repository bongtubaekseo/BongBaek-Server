package org.appjam.bongbaek.domain.event.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.appjam.bongbaek.domain.event.code.EventSuccessCode;
import org.appjam.bongbaek.domain.event.dto.request.CostProposalRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventDeleteRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventUpdateRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventWriteDto;
import org.appjam.bongbaek.domain.event.dto.response.CostProposalResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventDetailResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventHomeResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventListDto;
import org.appjam.bongbaek.domain.event.service.EventService;
import org.appjam.bongbaek.global.api.ApiResponse;
import org.appjam.bongbaek.global.api.ApiResponse.EmptyBody;
import org.appjam.bongbaek.global.common.CommonSuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmptyBody>> createEvent(
            @RequestHeader(name = "memberId") final String memberId,
            @RequestBody @Valid final EventWriteDto eventWriteDto
    ) {
        UUID testId = UUID.fromString(memberId);
        eventService.createEventInfo(testId, eventWriteDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(CommonSuccessCode.CREATED));
    }

    @GetMapping(path = "/history/{page}")
    public ResponseEntity<ApiResponse<EventListDto>> getEventHistory(
            @RequestHeader(name = "memberId") final String memberId,
            @PathVariable(name = "page") int page,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "attended", required = false) Boolean attended
    ) {
        UUID testId = UUID.fromString(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.OK,
                        eventService.getEventHistory(testId, page, category, attended)));
    }

    @GetMapping(path = "/upcoming/{page}")
    public ResponseEntity<ApiResponse<EventListDto>> getUpcomingEvents(
            @RequestHeader(name = "memberId") final String memberId,
            @PathVariable(name = "page") final int page,
            @RequestParam(name = "category", required = false) final String category
    ) {
        UUID testId = UUID.fromString(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.OK,
                        eventService.getUpcomingEvents(testId, page, category)));
    }

    @PostMapping(path = "/cost")
    public ResponseEntity<ApiResponse<CostProposalResponseDto>> createEventCost(
            @RequestHeader(name = "memberId") final String memberId,
            @RequestBody @Valid final CostProposalRequestDto costProposalRequestDto
    ){
        UUID testId = UUID.fromString(memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.OK,
                        eventService.getCostProposal(testId, costProposalRequestDto)));
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<ApiResponse<EventDetailResponseDto>> getEventByEventId(
            @PathVariable(name = "eventId") String eventId,   // NOTE: 클라 요청 간에는 무조건 String
            @RequestHeader(name = "memberId") String memberId    // TO DO: 멤버 JWT 구현 시 Refactor (현재 25-07-07 사용 X)
    ){

        UUID eventUUID = UUID.fromString(eventId);
        UUID memberUUID = UUID.fromString(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.GET_EVENT, eventService.getEventByEventId(eventUUID, memberUUID)));
    }

    @GetMapping(path = "/home")
    public ResponseEntity<ApiResponse<EventHomeResponseDto>> getEventsForHome(
            @RequestHeader(name = "memberId") String memberId
    ){

        UUID memberUUID = UUID.fromString(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.GET_EVENT, eventService.getEventsForHome(LocalDate.now(), memberUUID)));
    }

    @PutMapping(path = "/{eventId}")
    public ResponseEntity<ApiResponse<EmptyBody>> updateEvent(
            @PathVariable(name = "eventId") String eventId,
            @RequestHeader(name = "memberId") String memberId,
            @RequestBody @Valid EventUpdateRequestDto request
    ){
        UUID eventUUID = UUID.fromString(eventId);
        UUID memberUUID = UUID.fromString(memberId);

        eventService.updateEventByEventId(eventUUID, memberUUID, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.UPDATED_EVENT));
    }

    @DeleteMapping(path = "/{eventId}")
    public ResponseEntity<ApiResponse<EmptyBody>> deleteEventByEventId(
            @PathVariable(name = "eventId") String eventId,
            @RequestHeader(name = "memberId") String memberId
    ) {

        UUID eventUUID = UUID.fromString(eventId);
        UUID memberUUID = UUID.fromString(memberId);

        eventService.deleteEventByEventId(eventUUID, memberUUID);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.DELETED_EVENT));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<EmptyBody>> deleteEvents(
            @RequestHeader(name = "memberId") String memberId,
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
