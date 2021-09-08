package kr.startoff.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.startoff.backend.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@EntityGraph(attributePaths = {"parent"},type = EntityGraph.EntityGraphType.LOAD)
	Optional<Comment> findWithParentById(Long commentId);
}
