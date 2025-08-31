package org.appjam.bongbaek.domain.event.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.appjam.bongbaek.domain.event.dto.request.CostProposalRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventDeleteRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventUpdateRequestDto;
import org.appjam.bongbaek.domain.event.dto.request.EventWriteDto;
import org.appjam.bongbaek.domain.event.dto.response.CostProposalResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventDetailResponseDto;
import org.appjam.bongbaek.domain.event.dto.response.EventListDto;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.appjam.bongbaek.domain.event.exception.NotFoundEventException;
import org.appjam.bongbaek.domain.member.repository.MemberRepository;
import org.appjam.bongbaek.domain.event.service.util.CostCalculator;
import org.appjam.bongbaek.domain.event.service.util.RangeCalculator;
import org.appjam.bongbaek.domain.event.service.util.vo.CostParamInfo;
import org.appjam.bongbaek.domain.event.service.util.vo.RangeInfo;
import org.appjam.bongbaek.domain.member.entity.Member;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import org.appjam.bongbaek.domain.event.dto.response.EventHomeResponseDto;
import org.appjam.bongbaek.domain.event.entity.Event;
import org.appjam.bongbaek.domain.event.repository.EventRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private static final int PAGE_SIZE = 10;

    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createEventInfo(
        final String memberId,
        final EventWriteDto eventWriteDto
    ) {
        assertMemberExists(memberId);

        Member memberProxy = memberRepository.getReferenceById(memberId);
        Event event = eventWriteDto.toEntity(memberProxy);

        eventRepository.save(event);
    }

    public EventListDto getEventHistory(
        final String memberId,
        final int page,
        final String category,
        final Boolean attended
    ) {
        assertMemberExists(memberId);

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Slice<Event> result = eventRepository.findEventHistoryByMemberIdAndCategoryAndAttendedOrderBy(
            memberId,
            Category.of(category),
            attended,
            pageable
        );

        return EventListDto.of(result);
    }

    public EventListDto getUpcomingEvents(
        final String memberId,
        final int page,
        final String category
    ) {
        assertMemberExists(memberId);

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Slice<Event> result = eventRepository.findUpcomingEventsByMemberIdAndCategoryOrderBy(
            memberId,
            Category.of(category),
            pageable
        );

        return EventListDto.of(result);
    }

    public CostProposalResponseDto getCostProposal(
        String memberId,
        CostProposalRequestDto costProposalRequestDto
    ) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(CommonErrorCode.MEMBER_NOT_FOUND));

        int cost = CostCalculator.calculateCost(member, costProposalRequestDto);

        RangeInfo range = RangeCalculator.calculateRange(cost);

        CostParamInfo costParams = CostParamInfo.of(member, costProposalRequestDto);

        // LocationInfo가 null인 경우 반환값 임시
        if (costProposalRequestDto.locationInfo() == null) {
            return CostProposalResponseDto.of(
                cost,
                range,
                costProposalRequestDto.category(),
                null,
                costParams
            );
        }

        return CostProposalResponseDto.of(
            cost,
            range,
            costProposalRequestDto.category(),
            costProposalRequestDto.locationInfo().location(),
            costParams
        );
    }

    public EventDetailResponseDto getEventByEventId(
        String eventId,
        String memberId
    ) {
        Event event = eventRepository.findEventByEventIdAndMemberMemberId(eventId, memberId)
            .orElseThrow(NotFoundEventException::new);

        return EventDetailResponseDto.of(event);
    }


    public EventHomeResponseDto getEventsForHome(
        LocalDate now,
        String memberId
    ) {
        assertMemberExists(memberId);

        List<Event> events = eventRepository.findTop3ByEventDateGreaterThanEqualAndMemberMemberIdOrderByEventDateAsc(now, memberId);

        return EventHomeResponseDto.from(events);
    }

    @Transactional
    public void updateEventByEventId(
        String eventId,
        String memberId,
        EventUpdateRequestDto request
    ) {
        Event event = eventRepository.findEventByEventIdAndMemberMemberId(eventId, memberId)
            .orElseThrow(NotFoundEventException::new);

        event.updateFromDto(request);
    }

    @Transactional
    public void deleteEventByEventId(
        String eventId,
        String memberId
    ) {
        Event event = eventRepository.findEventByEventIdAndMemberMemberId(eventId, memberId)
            .orElseThrow(NotFoundEventException::new);

        eventRepository.delete(event);
    }

    @Transactional
    public void deleteEvents(
        EventDeleteRequestDto eventDeleteRequest,
        String memberId
    ) {
        List<Event> events = eventRepository.findAllByEventIdInAndMemberMemberId(eventDeleteRequest.eventIds(), memberId);

        if (events.size() != eventDeleteRequest.eventIds().size()) {
            throw new NotFoundEventException();
        }

        eventRepository.deleteAll(events);
    }

    private void assertMemberExists(
        final String memberId
    ) {
        if (!memberRepository.existsById(memberId)) {
            throw new CustomException(CommonErrorCode.MEMBER_NOT_FOUND);
        }
    }
}