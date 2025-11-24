package org.appjam.bongbaek.domain.event.controller;

import java.time.LocalDate;

import jakarta.validation.Valid;

import org.appjam.bongbaek.domain.event.dto.request.CostProposalRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventDeleteRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventUpdateRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventWriteDto;
import org.appjam.bongbaek.domain.event.dto.response.CostProposalResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventDetailResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventHomeResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventListDto;
import org.appjam.bongbaek.domain.event.service.EventService;
import org.appjam.bongbaek.global.api.code.event.SuccessCode;
import org.appjam.bongbaek.global.api.response.ApiResponse;
import org.appjam.bongbaek.global.api.response.SuccessResponse;
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
public class EventControllerImpl implements EventController {

	private final EventService eventService;

	@PostMapping
	public SuccessResponse<Void> createEvent(
			@AuthenticationPrincipal final String memberId,
			@RequestBody @Valid final EventWriteDto eventWriteDto
	) {
		eventService.createEventInfo(memberId, eventWriteDto);

		return ApiResponse.success(SuccessCode.EVENT_CREATED);
	}

	@GetMapping(path = "/history/{page}")
	public SuccessResponse<EventListDto> getEventHistory(
			@AuthenticationPrincipal final String memberId,
			@PathVariable(name = "page") final int page,
			@RequestParam(name = "category", required = false) final String category,
			@RequestParam(name = "attended", required = false) final Boolean attended
	) {
		return ApiResponse.success(SuccessCode.EVENT_FOUND,
				eventService.getEventHistory(memberId, page, category, attended));
	}

	@GetMapping(path = "/upcoming/{page}")
	public SuccessResponse<EventListDto> getUpcomingEvents(
			@AuthenticationPrincipal final String memberId,
			@PathVariable(name = "page") final int page,
			@RequestParam(name = "category", required = false) final String category
	) {
		return ApiResponse.success(SuccessCode.EVENT_FOUND, eventService.getUpcomingEvents(memberId, page, category));
	}

	@PostMapping(path = "/cost")
	public SuccessResponse<CostProposalResponseDto> createEventCost(
			@AuthenticationPrincipal final String memberId,
			@RequestBody @Valid final CostProposalRequestDto costProposalRequestDto
	) {
		return ApiResponse.success(SuccessCode.COST_CALCULATED,
				eventService.getCostProposal(memberId, costProposalRequestDto));
	}

	@GetMapping(path = "/{eventId}")
	public SuccessResponse<EventDetailResponseDto> getEventByEventId(
			@AuthenticationPrincipal final String memberId,
			@PathVariable(name = "eventId") String eventId   // NOTE: 클라 요청 간에는 무조건 String
	) {
		return ApiResponse.success(SuccessCode.EVENT_FOUND, eventService.getEventByEventId(eventId, memberId));
	}

	@GetMapping(path = "/home")
	public SuccessResponse<EventHomeResponseDto> getEventsForHome(
			@AuthenticationPrincipal final String memberId
	) {
		return ApiResponse.success(SuccessCode.EVENT_FOUND, eventService.getEventsForHome(LocalDate.now(), memberId));
	}

	@PutMapping(path = "/{eventId}")
	public SuccessResponse<Void> updateEvent(
			@AuthenticationPrincipal final String memberId,
			@PathVariable(name = "eventId") final String eventId,
			@RequestBody @Valid final EventUpdateRequestDto request
	) {
		eventService.updateEventByEventId(eventId, memberId, request);

		return ApiResponse.success(SuccessCode.EVENT_UPDATED);
	}

	@DeleteMapping(path = "/{eventId}")
	public SuccessResponse<Void> deleteEventByEventId(
			@AuthenticationPrincipal final String memberId,
			@PathVariable(name = "eventId") String eventId
	) {
		eventService.deleteEventByEventId(eventId, memberId);

		return ApiResponse.success(SuccessCode.EVENT_DELETED);
	}

	@DeleteMapping
	public SuccessResponse<Void> deleteEvents(
			@AuthenticationPrincipal final String memberId,
			@RequestBody EventDeleteRequestDto eventDeleteRequest
	) {
		eventService.deleteEvents(eventDeleteRequest, memberId);

		return ApiResponse.success(SuccessCode.EVENT_DELETED);
	}
}
