package org.appjam.bongbaek.domain.event.controller;

import org.appjam.bongbaek.domain.event.dto.request.CostProposalRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventDeleteRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventUpdateRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventWriteDto;
import org.appjam.bongbaek.domain.event.dto.response.CostProposalResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventDetailResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventHomeResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventListDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.appjam.bongbaek.global.api.response.SuccessResponse;

@Tag(name = "경조사 정보", description = "경조사 정보 관련 API")
public interface EventController {
	@Operation(summary = "경조사 정보 생성", description = "경조사 정보 데이터를 생성합니다.")
	SuccessResponse<Void> createEvent(String memberId, EventWriteDto eventWriteDto);

	@Operation(summary = "과거 경조사 정보 조회", description = "조회 시점을 기준으로 과거의 경조사 정보를 조회합니다.")
	SuccessResponse<EventListDto> getEventHistory(String memberId, int page, String category, Boolean attended);

	@Operation(summary = "다가올 경조사 정보 조회", description = "조회 시점을 기준으로 다가올 경조사 정보를 조회합니다.")
	SuccessResponse<EventListDto> getUpcomingEvents(String memberId, int page, String category);

	@Operation(summary = "경조사 비용 추천", description = "경조사 정보와 사용자 정보를 기반으로 적절한 경조사 비용을 산정합니다.")
	SuccessResponse<CostProposalResponseDto> createEventCost(String memberId, CostProposalRequestDto costProposalRequestDto);

	@Operation(summary = "경조사 정보 상세 조회", description = "단일 경조사 정보에 대한 상세 정보를 조회합니다.")
	SuccessResponse<EventDetailResponseDto> getEventByEventId(String memberId, String eventId);

	@Operation(summary = "홈화면에서의 경조사 정보 조회", description = "홈화면에 표시하기 위한 경조사 정보를 조회합니다.")
	SuccessResponse<EventHomeResponseDto> getEventsForHome(String memberId);

	@Operation(summary = "경조사 정보 수정", description = "단일 경조사 정보에 대한 내용을 수정합니다.")
	SuccessResponse<Void> updateEvent(String memberId, String eventId, EventUpdateRequestDto eventUpdateRequestDto);

	@Operation(summary = "단일 경조사 정보 삭제", description = "단일 경조사 정보에 대한 데이터를 삭제합니다.")
	SuccessResponse<Void> deleteEventByEventId(String memberId, String eventId);

	@Operation(summary = "복수 경조사 정보 삭제", description = "여러 개의 경조사 정보에 대한 데이터를 삭제합니다.")
	SuccessResponse<Void> deleteEvents(String memberId, EventDeleteRequestDto eventDeleteRequestDto);
}
