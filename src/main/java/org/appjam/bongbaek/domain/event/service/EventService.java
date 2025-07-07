package org.appjam.bongbaek.domain.event.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.appjam.bongbaek.domain.event.util.EventValidator.EventAuthorization;
import org.appjam.bongbaek.domain.event.dto.EventDetailResponseDto;
import org.springframework.transaction.annotation.Transactional;
import org.appjam.bongbaek.domain.event.dto.EventHomeResponseDto;
import org.appjam.bongbaek.domain.event.entity.Event;
import org.appjam.bongbaek.domain.event.exception.NotFoundEventException;
import org.appjam.bongbaek.domain.event.repository.EventRepository;
import org.appjam.bongbaek.domain.event.util.EventValidator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public EventDetailResponseDto getEventByEventId(UUID eventUUID, UUID memberUUID) {

        Event event = eventRepository.findById(eventUUID)
                .orElseThrow(NotFoundEventException::new);

        EventAuthorization(memberUUID, event.getMember().getMemberId());

        return EventDetailResponseDto.of(event);

    }

    @Transactional(readOnly = true)
    public List<EventHomeResponseDto> getEventsForHome(LocalDate now, UUID memberUUID){

        List<Event> events = eventRepository.findTop3ByEventDateAfterOrderByEventDateDesc(now);

        for (Event event : events) {
            EventValidator.EventAuthorization(memberUUID, event.getMember().getMemberId());
        }

        return events.stream()
                .map(EventHomeResponseDto::of)
                .toList();
    }

    @Transactional
    public void deleteEventByEventId(UUID eventUUID, UUID memberUUID) {

        Event event = eventRepository.findById(eventUUID)
                .orElseThrow(NotFoundEventException::new);

        EventValidator.EventAuthorization(memberUUID, event.getMember().getMemberId());

        eventRepository.delete(event);
    }

    @Transactional
    public void deleteEvents(List<UUID> eventList, UUID memberUUID) {

        for (UUID eventUUID : eventList) {
            Event event = eventRepository.findById(eventUUID)
                    .orElseThrow(NotFoundEventException::new);

            EventValidator.EventAuthorization(memberUUID, event.getMember().getMemberId());

            eventRepository.delete(event);
        }
    }
}
