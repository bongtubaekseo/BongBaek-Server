package org.appjam.bongbaek.domain.event.repository;

import org.appjam.bongbaek.domain.common.Category;
import org.appjam.bongbaek.domain.event.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface EventSearchRepository {
	Slice<Event> findEventHistoryByMemberIdAndCategoryAndAttendedOrderBy(String memberId, Category category, Boolean attended, Pageable pageable);

	Slice<Event> findUpcomingEventsByMemberIdAndCategoryOrderBy(String memberId, Category category, Pageable pageable);
}
