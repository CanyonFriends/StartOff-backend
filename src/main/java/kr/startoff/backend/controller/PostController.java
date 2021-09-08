package kr.startoff.backend.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.startoff.backend.entity.Category;
import kr.startoff.backend.entity.Post;
import kr.startoff.backend.exception.custom.CategoryNotFoundException;
import kr.startoff.backend.payload.request.PostRequest;
import kr.startoff.backend.payload.response.CommonResponse;
import kr.startoff.backend.payload.response.PostListResponse;
import kr.startoff.backend.payload.response.PostResponse;
import kr.startoff.backend.repository.PostQueryRepository;
import kr.startoff.backend.service.PostService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostController {
	private final PostService postService;

	@PostMapping("/posts")
	public ResponseEntity<PostResponse> save(@RequestBody PostRequest postRequest) {
		Post post = postService.savePost(postRequest);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/posts/")
			.buildAndExpand(post.getId()).toUri();

		return ResponseEntity.created(location).body(new PostResponse(post));
	}

	@PutMapping("/posts/{post_id}")
	public ResponseEntity<PostResponse> update(@PathVariable(value = "post_id") Long postId,
		@RequestBody PostRequest postRequest) {
		return ResponseEntity.ok(postService.updatePost(postId, postRequest));
	}

	@DeleteMapping("/posts/{post_id}")
	public ResponseEntity<CommonResponse> delete(@PathVariable(value = "post_id") Long postId) {
		postService.deletePost(postId);
		return ResponseEntity.ok(new CommonResponse(true, "성공적으로 삭제되었습니다."));
	}

	@GetMapping("/posts")
	public ResponseEntity<Page<PostListResponse>> getPosts(
		@RequestParam(value = "category") Optional<String> categoryCandidate, Pageable pageable) {
		if (categoryCandidate.isPresent()) {
			if (Category.isCategory(categoryCandidate.get())) {
				Category category = Category.valueOf(categoryCandidate.get().toUpperCase());
				return ResponseEntity.ok(postService.readPostsByCategory(category, pageable));
			}
			throw new CategoryNotFoundException();
		}
		return ResponseEntity.ok(postService.readPosts(pageable));
	}

	@GetMapping("/posts/{post_id}")
	public ResponseEntity<PostResponse> getPost(@PathVariable(value = "post_id") Long postId) {
		return ResponseEntity.ok(postService.readPost(postId));
	}

	@GetMapping("/search")
	public ResponseEntity<Void> search(@RequestParam Optional<String> author, @RequestParam Optional<String> title,
		@RequestParam Optional<String> content, @RequestParam Optional<String> skill) {
		postService.search(author.isPresent(),title.isPresent(),content.isPresent(),skill.isPresent());

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/categories")
	public ResponseEntity<List<String>> getCategories() {
		Class<Category> category = Category.class;
		return ResponseEntity.ok(Arrays
			.stream(category.getEnumConstants())
			.map(Enum::toString)
			.map(String::toLowerCase)
			.collect(Collectors.toList())
		);
	}
}
