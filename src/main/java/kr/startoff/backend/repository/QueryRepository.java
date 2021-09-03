package kr.startoff.backend.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.startoff.backend.entity.Post;
import kr.startoff.backend.entity.QPost;
import kr.startoff.backend.entity.QSkillTag;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public List<Post> findAllByQuery(String d){
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(QPost.post.title.like("%"+d+"%"));
		builder.or(QPost.post.author.nickname.like("%"+d+"%"));
		builder.or(QPost.post.content.like("%"+d+"%"));
		return jpaQueryFactory.selectFrom(QPost.post)
			.innerJoin(QPost.post.postWantedSkills,QSkillTag.skillTag)
			.where(builder)
			.fetchJoin()
			.fetch();
	}
}
