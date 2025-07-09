package org.appjam.bongbaek.domain.event.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.appjam.bongbaek.domain.event.entity.Category;
import org.appjam.bongbaek.domain.event.entity.Event;
import org.appjam.bongbaek.domain.event.entity.QEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EventSearchRepositoryImpl implements EventSearchRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<Event> findEventHistoryByMemberIdAndCategoryAndAttendedOrderBy(
			UUID memberId,
			Category category,
			Boolean attended,
			Pageable pageable
	) {
		QEvent qEvent = QEvent.event;
		int pageSize = pageable.getPageSize();

		List<Event> events = queryFactory.selectFrom(qEvent)
				.where(
						qEvent.member.memberId.eq(memberId),
						qEvent.eventDate.before(LocalDate.now()),
						categoryEqual(category),
						attendedEqual(attended)
				)
				.orderBy(qEvent.eventDate.desc())
				.offset(pageable.getOffset())
				.limit(pageSize + 1)
				.fetch();

		boolean hasNext = events.size() > pageSize;
		if (hasNext) {
			events.remove(pageSize);
		}

		return new SliceImpl<Event>(events, pageable, hasNext);
	}

	@Override
	public Slice<Event> findUpcomingEventsByMemberIdAndCategoryOrderBy(UUID memberId, Category category,
			Pageable pageable) {
		QEvent qEvent = QEvent.event;
		int pageSize = pageable.getPageSize();

		List<Event> events = queryFactory.selectFrom(qEvent)
				.where(
						qEvent.member.memberId.eq(memberId),
						qEvent.eventDate.after(LocalDate.now()),
						categoryEqual(category)
				)
				.orderBy(qEvent.eventDate.asc())
				.offset(pageable.getOffset())
				.limit(pageSize + 1)
				.fetch();

		boolean hasNext = events.size() > pageSize;
		if (hasNext) {
			events.remove(pageSize);
		}

		return new SliceImpl<Event>(events, pageable, hasNext);
	}

	private BooleanExpression categoryEqual(Category category) {
		if(category == null){
			return null;
		}
		return QEvent.event.eventCategory.eq(category);
	}

	private BooleanExpression attendedEqual(Boolean attended) {
		if(attended == null){
			return null;
		}
		return QEvent.event.attended.eq(attended);
	}
}
