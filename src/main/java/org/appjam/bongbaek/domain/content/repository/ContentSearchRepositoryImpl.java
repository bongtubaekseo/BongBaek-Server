package org.appjam.bongbaek.domain.content.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.domain.content.entity.Content;
import org.appjam.bongbaek.domain.content.entity.QContent;
import org.appjam.bongbaek.domain.event.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContentSearchRepositoryImpl implements ContentSearchRepository {

    private final JPAQueryFactory queryFactory;
    private final QContent qContent = QContent.content;

    @Override
    public Page<Content> findContentsByCategoryOrderByCreatedDateDesc(Category category, Pageable pageable) {

        List<Content> contents = queryFactory.selectFrom(qContent)
                .where(categoryEqual(category))
                .orderBy(qContent.createdDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(qContent.count())
                .from(qContent)
                .where(categoryEqual(category))
                .fetchOne();

        if(totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(contents, pageable, totalCount);
    }

    private BooleanExpression categoryEqual(Category category) {
        if(category == null){
            return null;
        }
        return QContent.content.contentCategory.eq(category);
    }
}
