package org.appjam.bongbaek.domain.event.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.appjam.bongbaek.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, String>, EventSearchRepository{

    @Query("SELECT e FROM Event e INNER JOIN e.member m WHERE e.eventDate >= :date AND m.memberId = :memberId ORDER BY e.eventDate ASC")
    List<Event> findTop3ByEventDateAndMemberId(@Param("date") LocalDate date, @Param("memberId") String memberId);

    Optional<Event> findEventByEventIdAndMemberMemberId(String eventId, String memberMemberId);

    List<Event> findAllByEventIdInAndMemberMemberId(List<String> eventIds, String memberId);
}
