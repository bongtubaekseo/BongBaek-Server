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

    List<Event> findTop3ByEventDateGreaterThanEqualAndMemberMemberIdOrderByEventDateAsc(LocalDate now, String member_memberId); // TO DO: jwt 발급 시 사용

    @Query(value = "select * from event e where e.event_id = :eventId and e.member_id = :memberId", nativeQuery = true)
    Optional<Event> findEventByEventIdAndMemberMemberId(@Param("eventId") String eventId, @Param("memberId") String memberId);

    List<Event> findAllByEventIdInAndMemberMemberId(List<String> eventIds, String memberId);
}
