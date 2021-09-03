package kr.startoff.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.startoff.backend.entity.Category;
import kr.startoff.backend.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findAllByCategory(Category category, Pageable pageable);
}
