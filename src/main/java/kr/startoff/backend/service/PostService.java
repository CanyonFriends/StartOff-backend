package kr.startoff.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.entity.Category;
import kr.startoff.backend.entity.Comment;
import kr.startoff.backend.entity.Post;
import kr.startoff.backend.entity.SkillTag;
import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.custom.PostNotFoundException;
import kr.startoff.backend.exception.custom.UserNotFoundException;
import kr.startoff.backend.payload.request.PostRequest;
import kr.startoff.backend.payload.response.CommentResponse;
import kr.startoff.backend.payload.response.PostListResponse;
import kr.startoff.backend.payload.response.PostResponse;
import kr.startoff.backend.repository.CommentQueryRepository;
import kr.startoff.backend.repository.PostQueryRepository;
import kr.startoff.backend.repository.PostRepository;
import kr.startoff.backend.repository.SkillTagRepository;
import kr.startoff.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final SkillTagRepository skillTagRepository;
	private final CommentQueryRepository commentQueryRepository;
	private final PostQueryRepository postQueryRepository;

	@Transactional
	public Post savePost(PostRequest postRequest) {
		User author = userRepository.findById(postRequest.getUserId()).orElseThrow(UserNotFoundException::new);
		List<SkillTag> postWantedSkills = extractPostSkills(postRequest);
		Post post = Post.createPost(author, postRequest, postWantedSkills);
		return postRepository.save(post);
	}

	@Transactional
	public PostResponse updatePost(Long postId, PostRequest postRequest) {
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		List<SkillTag> postWantedSkills = extractPostSkills(postRequest);
		post.updatePost(postRequest, postWantedSkills);
		return new PostResponse(post);
	}

	@Transactional
	public Long deletePost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow();
		postRepository.delete(post);
		return postId;
	}

	@Transactional(readOnly = true)
	public PostResponse readPost(Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		PostResponse postResponse = new PostResponse(post);
		postResponse.setComments(convertNestedStructure(commentQueryRepository.findAllByPostId(postId)));
		return postResponse;
	}

	@Transactional(readOnly = true)
	public Page<PostListResponse> readPosts(Pageable pageable) {
		Page<Post> posts = postRepository.findAll(pageable);
		return posts.map(PostListResponse::new);
	}

	@Transactional(readOnly = true)
	public Page<PostListResponse> readPostsByCategory(Category category, Pageable pageable) {
		Page<Post> posts = postRepository.findAllByCategory(category, pageable);
		return posts.map(PostListResponse::new);
	}

	@Transactional(readOnly = true)
	public Page<PostListResponse> searchByCategory(String query, List<String> types, Category category,
		Pageable pageable) {
		List<Long> postIds = postQueryRepository.findAllByQueryWithCategory(query, category, types);
		return postRepository.findAllByIdIn(postIds, pageable).map(PostListResponse::new);
	}

	@Transactional(readOnly = true)
	public Page<PostListResponse> search(String query, List<String> types, Pageable pageable) {
		List<Long> postIds = postQueryRepository.findAllByQuery(query, types);
		return postRepository.findAllByIdIn(postIds, pageable).map(PostListResponse::new);
	}

	private List<SkillTag> extractPostSkills(PostRequest postRequest) {
		return postRequest.getPostSkills()
			.stream()
			.map(skillTagRepository::findBySkillName)
			.distinct()
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
	}

	private List<CommentResponse> convertNestedStructure(List<Comment> comments) {
		List<CommentResponse> result = new ArrayList<>();
		Map<Long, CommentResponse> map = new HashMap<>();
		comments.forEach(c -> {
			CommentResponse dto = new CommentResponse(c);
			map.put(dto.getCommentId(), dto);
			if (c.getParent() != null) {
				map.get(c.getParent().getId()).addChildComment(dto);
			} else {
				result.add(dto);
			}
		});
		return result;
	}

}
