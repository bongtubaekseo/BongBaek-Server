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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            @AuthenticationPrincipal final UUID memberId,
            @RequestBody @Valid final EventWriteDto eventWriteDto
    ) {
        eventService.createEventInfo(memberId, eventWriteDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(CommonSuccessCode.CREATED));
    }

    @GetMapping(path = "/history/{page}")
    public ResponseEntity<ApiResponse<EventListDto>> getEventHistory(
            @AuthenticationPrincipal final UUID memberId,
            @PathVariable(name = "page") final int page,
            @RequestParam(name = "category", required = false) final String category,
            @RequestParam(name = "attended", required = false) final Boolean attended
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.OK,
                        eventService.getEventHistory(memberId, page, category, attended)));
    }

    @GetMapping(path = "/upcoming/{page}")
    public ResponseEntity<ApiResponse<EventListDto>> getUpcomingEvents(
            @AuthenticationPrincipal final UUID memberId,
            @PathVariable(name = "page") final int page,
            @RequestParam(name = "category", required = false) final String category
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.OK,
                        eventService.getUpcomingEvents(memberId, page, category)));
    }

    @PostMapping(path = "/cost")
    public ResponseEntity<ApiResponse<CostProposalResponseDto>> createEventCost(
            @AuthenticationPrincipal final UUID memberId,
            @RequestBody @Valid final CostProposalRequestDto costProposalRequestDto
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(CommonSuccessCode.OK,
                        eventService.getCostProposal(memberId, costProposalRequestDto)));
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<ApiResponse<EventDetailResponseDto>> getEventByEventId(
            @AuthenticationPrincipal final UUID memberId,
            @PathVariable(name = "eventId") UUID eventId   // NOTE: 클라 요청 간에는 무조건 String
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.GET_EVENT, eventService.getEventByEventId(eventId, memberId)));
    }

    @GetMapping(path = "/home")
    public ResponseEntity<ApiResponse<EventHomeResponseDto>> getEventsForHome(
            @AuthenticationPrincipal final UUID memberId
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.GET_EVENT, eventService.getEventsForHome(LocalDate.now(), memberId)));
    }

    @PutMapping(path = "/{eventId}")
    public ResponseEntity<ApiResponse<EmptyBody>> updateEvent(
            @AuthenticationPrincipal final UUID memberId,
            @PathVariable(name = "eventId") final UUID eventId,
            @RequestBody @Valid final EventUpdateRequestDto request
    ){
        eventService.updateEventByEventId(eventId, memberId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.UPDATED_EVENT));
    }

    @DeleteMapping(path = "/{eventId}")
    public ResponseEntity<ApiResponse<EmptyBody>> deleteEventByEventId(
            @AuthenticationPrincipal final UUID memberId,
            @PathVariable(name = "eventId") UUID eventId
    ) {
        eventService.deleteEventByEventId(eventId, memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.DELETED_EVENT));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<EmptyBody>> deleteEvents(
            @AuthenticationPrincipal final UUID memberId,
            @RequestBody EventDeleteRequestDto eventDeleteRequest
    ){

        eventService.deleteEvents(eventDeleteRequest, memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(EventSuccessCode.DELETED_EVENT));
    }
}
