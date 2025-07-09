package org.appjam.bongbaek.domain.event.repository;

import java.util.UUID;

import org.appjam.bongbaek.domain.event.entity.Category;
import org.appjam.bongbaek.domain.event.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface EventSearchRepository {
	Slice<Event> findEventHistoryByMemberIdAndCategoryAndAttendedOrderBy(UUID memberId, Category category, Boolean attended, Pageable pageable);

	Slice<Event> findUpcomingEventsByMemberIdAndCategoryOrderBy(UUID memberId, Category category, Pageable pageable);
}
