package kr.startoff.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.startoff.backend.entity.Category;
import kr.startoff.backend.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	Page<Post> findAllByCategory(Category category, Pageable pageable);
	Page<Post> findAllByIdIn(List<Long> ids, Pageable pageable);
}
