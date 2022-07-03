package kr.startoff.backend.domain.post.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.startoff.backend.domain.post.domain.Category;
import kr.startoff.backend.domain.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findAllByCategory(Category category, Pageable pageable);
	Page<Post> findAllByIdIn(List<Long> ids, Pageable pageable);
}
