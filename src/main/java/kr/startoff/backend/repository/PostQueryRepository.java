package kr.startoff.backend.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.startoff.backend.entity.Category;
import kr.startoff.backend.entity.QPost;
import kr.startoff.backend.entity.QSkillTag;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public List<Long> findAllByQueryWithCategory(String query, Category category, List<String> types) {
		BooleanBuilder builder = makeBooleanBuilder(query, types);
		builder.and(QPost.post.category.eq(category));
		return getResult(builder);
	}

	public List<Long> findAllByQuery(String query, List<String> types) {
		BooleanBuilder builder = makeBooleanBuilder(query, types);
		return getResult(builder);
	}

	private List<Long> getResult(BooleanBuilder builder) {
		return jpaQueryFactory.select(QPost.post.id)
			.from(QPost.post)
			.leftJoin(QPost.post.postWantedSkills, QSkillTag.skillTag)
			.where(builder)
			.distinct()
			.orderBy(QPost.post.id.asc())
			.fetch();
	}

	private BooleanBuilder makeBooleanBuilder(String query, List<String> types) {
		BooleanBuilder builder = new BooleanBuilder();
		for (String type : types) {
			switch (type.toUpperCase()) {
				case "TITLE":
					builder.or(QPost.post.title.like("%" + query + "%"));
					break;
				case "CONTENT":
					builder.or(QPost.post.content.like("%" + query + "%"));
					break;
				case "SKILL":
					builder.or(QSkillTag.skillTag.skillName.like("%" + query + "%"));
					break;
				default:
					break;
			}
		}
		return builder;
	}
}
