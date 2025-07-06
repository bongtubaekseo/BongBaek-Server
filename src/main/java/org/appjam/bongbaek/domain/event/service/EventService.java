package org.appjam.bongbaek.domain.event.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.appjam.bongbaek.domain.event.dto.EventDetailResponseDto;
import org.appjam.bongbaek.domain.event.dto.EventHomeResponseDto;
import org.appjam.bongbaek.domain.event.entity.Event;
import org.appjam.bongbaek.domain.event.repository.EventRepository;
import org.appjam.bongbaek.domain.member.entity.Member;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventDetailResponseDto getEventByEventId(UUID targetId){

        Event event = eventRepository.findById(targetId)
                .orElseThrow(RuntimeException::new);

        return EventDetailResponseDto.of(event);
    }

    public List<EventHomeResponseDto> getEventsForHome(Member member, LocalDate now){

        List<Event> events = eventRepository.findTop3ByEventDateAfterOrderByEventDateDesc(now);

        return events.stream()
                .map(EventHomeResponseDto::of)
                .toList();
    }
}
