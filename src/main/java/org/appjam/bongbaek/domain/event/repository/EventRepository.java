package org.appjam.bongbaek.domain.event.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.appjam.bongbaek.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findTop3ByEventDateGreaterThanEqualAndMemberMemberIdOrderByEventDateAsc(LocalDate now, UUID member_memberId); // TO DO: jwt 발급 시 사용

    Optional<Event> findEventByEventIdAndMemberMemberId(UUID eventId, UUID memberMemberId);

    List<Event> findAllByEventIdInAndMemberMemberId(List<UUID> eventIds, UUID memberId);
}
