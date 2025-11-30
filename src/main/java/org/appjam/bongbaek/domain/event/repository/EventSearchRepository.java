package org.appjam.bongbaek.domain.event.repository;

import org.appjam.bongbaek.domain.event.entity.Category;
import org.appjam.bongbaek.domain.event.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface EventSearchRepository {
	Slice<Event> findEventHistoryByMemberIdAndCategoryAndAttendedOrderBy(String memberId, Category category,
			Boolean attended, Pageable pageable);

	Slice<Event> findUpcomingEventsByMemberIdAndCategoryOrderBy(String memberId, Category category, Pageable pageable);

	Slice<Event> findMonthlyEventsByMemberIdAndCategoryAndAttendedOrderBy(String memberId, int year, int month,
			Category category, Boolean attended, Pageable pageable);
}