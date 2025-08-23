package org.appjam.bongbaek.domain.event.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.appjam.bongbaek.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, String>, EventSearchRepository{

    List<Event> findTop3ByEventDateGreaterThanEqualAndMemberMemberIdOrderByEventDateAsc(LocalDate now, String member_memberId); // TO DO: jwt 발급 시 사용

    Optional<Event> findEventByEventIdAndMemberMemberId(String eventId, String memberMemberId);

    List<Event> findAllByEventIdInAndMemberMemberId(List<String> eventIds, String memberId);
}