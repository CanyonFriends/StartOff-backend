package kr.startoff.backend.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.startoff.backend.entity.Comment;
import kr.startoff.backend.entity.QComment;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public List<Comment> findAllByPostId(Long postId){
		return jpaQueryFactory.selectFrom(QComment.comment)
			.leftJoin(QComment.comment.parent)
			.fetchJoin()
			.where(QComment.comment.post.id.eq(postId))
			.orderBy(
				QComment.comment.parent.id.asc().nullsFirst(),
				QComment.comment.createdAt.asc()
			).fetch();
	}
}