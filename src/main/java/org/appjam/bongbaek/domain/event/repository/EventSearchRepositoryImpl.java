package org.appjam.bongbaek.domain.event.repository;

import java.time.LocalDate;
import java.util.List;

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
			String memberId,
			Category category,
			Boolean attended,
			Pageable pageable
	) {
		QEvent qEvent = QEvent.event;
		int pageSize = pageable.getPageSize();

		List<Event> events = queryFactory.selectFrom(qEvent)
				.where(
						qEvent.member.memberId.eq(memberId),
						qEvent.eventDate.loe(LocalDate.now()),
						categoryEqual(category),
						attendedEqual(attended)
				)
				.orderBy(qEvent.eventDate.desc())
				.offset(pageable.getOffset())
				.limit(pageSize + 1)
				.fetch();

		return toSlice(events, pageable);
	}

	@Override
	public Slice<Event> findUpcomingEventsByMemberIdAndCategoryOrderBy(
			String memberId,
			Category category,
			Pageable pageable
	) {
		QEvent qEvent = QEvent.event;
		int pageSize = pageable.getPageSize();

		List<Event> events = queryFactory.selectFrom(qEvent)
				.where(
						qEvent.member.memberId.eq(memberId),
						qEvent.eventDate.goe(LocalDate.now()),
						categoryEqual(category)
				)
				.orderBy(qEvent.eventDate.asc())
				.offset(pageable.getOffset())
				.limit(pageSize + 1)
				.fetch();

		return toSlice(events, pageable);
	}

	@Override
	public Slice<Event> findMonthlyEventsByMemberIdAndCategoryAndAttentedOrderBy(
			String memberId,
			int year,
			int month,
			Category category,
			Boolean attended,
			Pageable pageable
	) {
		QEvent qEvent = QEvent.event;
		int pageSize = pageable.getPageSize();

		List<Event> events = queryFactory.selectFrom(qEvent)
				.where(
						qEvent.member.memberId.eq(memberId),
						eventMonthEqual(year, month),
						categoryEqual(category),
						attendedEqual(attended)
				)
				.orderBy(qEvent.eventDate.desc())
				.offset(pageable.getOffset())
				.limit(pageSize + 1)
				.fetch();

		return toSlice(events, pageable);
	}

	private BooleanExpression categoryEqual(Category category) {
		if (category == null) {
			return null;
		}
		return QEvent.event.eventCategory.eq(category);
	}

	private BooleanExpression attendedEqual(Boolean attended) {
		if (attended == null) {
			return null;
		}
		return QEvent.event.attended.eq(attended);
	}

	private BooleanExpression eventMonthEqual(int year, int month) {
		if (year == 0 || month == 0 || month > 12) {
			return null;
		}

		LocalDate monthStart = LocalDate.of(year, month, 1);
		LocalDate monthEnd = monthStart.plusMonths(1);

		return QEvent.event.eventDate.goe(monthStart)
				.and(QEvent.event.eventDate.lt(monthEnd));
	}

	private <T> Slice<T> toSlice(List<T> list, Pageable pageable) {
		int pageSize = pageable.getPageSize();

		boolean hasNext = list.size() > pageSize;

		if (hasNext) list.remove(pageSize);

		return new SliceImpl<>(list, pageable, hasNext);
	}
}