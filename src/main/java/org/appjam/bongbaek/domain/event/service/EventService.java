package org.appjam.bongbaek.domain.event.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.appjam.bongbaek.domain.event.dto.request.EventUpdateRequestDto;
import org.appjam.bongbaek.domain.event.dto.response.EventDetailResponseDto;
import org.appjam.bongbaek.domain.event.exception.NotFoundEventException;
import org.appjam.bongbaek.domain.event.exception.UnauthorizationException;
import org.springframework.transaction.annotation.Transactional;
import org.appjam.bongbaek.domain.event.dto.response.EventHomeResponseDto;
import org.appjam.bongbaek.domain.event.entity.Event;
import org.appjam.bongbaek.domain.event.repository.EventRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public EventDetailResponseDto getEventByEventId(UUID eventUUID, UUID memberUUID) {

        Event event = eventRepository.findEventByEventIdAndMemberMemberId(eventUUID, memberUUID)
                .orElseThrow(NotFoundEventException::new);

        return EventDetailResponseDto.of(event);
    }

    @Transactional(readOnly = true)
    public EventHomeResponseDto getEventsForHome(LocalDate now, UUID memberUUID){

        List<Event> events = eventRepository.findTop3ByEventDateGreaterThanEqualAndMemberMemberIdOrderByEventDateAsc(now, memberUUID);

        return EventHomeResponseDto.from(events);
    }

    @Transactional
    public void updateEventByEventId(UUID eventUUID, UUID memberUUID, EventUpdateRequestDto request) {

        Event event = eventRepository.findEventByEventIdAndMemberMemberId(eventUUID, memberUUID)
                .orElseThrow(NotFoundEventException::new);

        event.updateFromDto(request);
    }

    @Transactional
    public void deleteEventByEventId(UUID eventUUID, UUID memberUUID) {

        Event event = eventRepository.findEventByEventIdAndMemberMemberId(eventUUID, memberUUID)
                .orElseThrow(NotFoundEventException::new);

        eventRepository.delete(event);
    }

    @Transactional
    public void deleteEvents(List<UUID> eventList, UUID memberUUID) {

        List<Event> events = eventRepository.findAllByEventIdInAndMemberMemberId(eventList, memberUUID);

        if (events.size() != eventList.size()) {
            throw new NotFoundEventException();
        }

        eventRepository.deleteAll(events);
    }
}
