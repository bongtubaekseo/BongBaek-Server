package org.appjam.bongbaek.domain.event.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.appjam.bongbaek.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findTop3ByEventDateAfterAndMemberMemberIdOrderByEventDateDesc(LocalDate now, UUID member_memberId); // TO DO: jwt 발급 시 사용
    List<Event> findTop3ByEventDateAfterOrderByEventDateDesc(LocalDate date);   // TEST용. 멤버 JWT 구현 시 삭제
}
