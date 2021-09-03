package kr.startoff.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.entity.Category;
import kr.startoff.backend.entity.Post;
import kr.startoff.backend.entity.SkillTag;
import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.custom.PostNotFoundException;
import kr.startoff.backend.exception.custom.UserNotFoundException;
import kr.startoff.backend.payload.request.PostRequest;
import kr.startoff.backend.payload.response.PostResponse;
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
		return new PostResponse(post);
	}

	@Transactional(readOnly = true)
	public Page<PostResponse> readPosts(Pageable pageable) {
		Page<Post> posts = postRepository.findAll(pageable);
		return posts.map(PostResponse::new);
	}

	@Transactional(readOnly = true)
	public Page<PostResponse> readPostsByCategory(Category category,Pageable pageable) {
		Page<Post> posts = postRepository.findAllByCategory(category, pageable);
		return posts.map(PostResponse::new);
	}

	@Transactional(readOnly = true)
	public void search(boolean author, boolean title, boolean content, boolean skill) {

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
}
