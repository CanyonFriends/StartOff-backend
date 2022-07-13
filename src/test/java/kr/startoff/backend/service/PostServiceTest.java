package kr.startoff.backend.service;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.post.service.PostService;
import kr.startoff.backend.domain.post.domain.Category;
import kr.startoff.backend.domain.post.domain.Post;
import kr.startoff.backend.domain.post.exception.PostNotFoundException;
import kr.startoff.backend.domain.post.dto.PostRequest;
import kr.startoff.backend.domain.post.dto.PostListResponse;
import kr.startoff.backend.domain.post.dto.PostResponse;
import kr.startoff.backend.domain.comment.repository.CommentQueryRepository;
import kr.startoff.backend.domain.post.repository.PostQueryRepository;
import kr.startoff.backend.domain.post.repository.PostRepository;
import kr.startoff.backend.domain.tag.repository.SkillTagRepository;
import kr.startoff.backend.domain.user.repository.UserRepository;

class PostServiceTest {
	private PostRepository postRepository;
	private UserRepository userRepository;
	private SkillTagRepository skillTagRepository;
	private CommentQueryRepository commentQueryRepository;
	private PostQueryRepository postQueryRepository;
	private PostService postService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		postRepository = mock(PostRepository.class);
		userRepository = mock(UserRepository.class);
		skillTagRepository = mock(SkillTagRepository.class);
		commentQueryRepository = mock(CommentQueryRepository.class);
		postQueryRepository = mock(PostQueryRepository.class);
		postService = new PostService(postRepository, userRepository, skillTagRepository, commentQueryRepository,
			postQueryRepository);
	}

	@Test
	void savePostTest() throws Exception {
		//given
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(getUser()));
		given(postRepository.save(any())).willReturn(getPost(postRequest()));
		//when
		Post createdPost = postService.savePost(postRequest());
		//then
		Post result = getPost(postRequest());
		assertNotNull(createdPost);
		assertEquals(createdPost.getAuthor().getId(), result.getAuthor().getId());
		assertEquals(createdPost.getTitle(), result.getTitle());
		assertEquals(createdPost.getContent(), result.getContent());
		assertEquals(createdPost.getCategory(), result.getCategory());
	}

	@Test
	void updatePostTest() throws Exception {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(getUser()));
		given(postRepository.findById(POST_ID)).willReturn(Optional.of(getPost(postRequest())));
		PostResponse updatePost = postService.updatePost(POST_ID, updatePostRequest());

		PostResponse result = updatePostResponse();

		assertNotNull(updatePost);

		assertEquals(updatePost.getPostId(), result.getPostId());
		assertEquals(updatePost.getTitle(), result.getTitle());
		assertEquals(updatePost.getContent(), result.getContent());
		assertEquals(updatePost.getCategory(), result.getCategory());

	}

	@Test
	void updatePostThrowExceptionTest() throws Exception {
		given(postRepository.findById(POST_ID)).willThrow(PostNotFoundException.class);
		PostRequest request = postRequest();
		assertThrows(PostNotFoundException.class,
			() -> postService.updatePost(POST_ID, request), "해당 글을 찾을 수 없습니다.");
	}

	@Test
	void readPostTest() throws Exception {
		given(postRepository.findById(POST_ID)).willReturn(Optional.of(getPost(postRequest())));
		given(commentQueryRepository.findAllByPostId(POST_ID)).willReturn(
			List.of(getParentComment(), getChildComment()));

		PostResponse postResponse = postService.readPost(POST_ID);

		assertEquals(objectMapper.writeValueAsString(postResponseSetComment()),
			objectMapper.writeValueAsString(postResponse));
	}

	@Test
	void readPostThrowExceptionTest() throws Exception {
		given(postRepository.findById(POST_ID)).willThrow(PostNotFoundException.class);

		assertThrows(PostNotFoundException.class,
			() -> postService.readPost(POST_ID), "해당 글을 찾을 수 없습니다.");
	}
	@Test
	void deletePostTest() throws Exception {
		Post post = getPost(postRequest());
		given(postRepository.findById(POST_ID)).willReturn(Optional.of(post));

		Long result = postService.deletePost(POST_ID);

		verify(postRepository, times(1)).delete(post);
		assertEquals(POST_ID, result);
	}

	@Test
	void searchTest() throws Exception {
		given(postQueryRepository.findAllByQuery(any(String.class), anyList())).willReturn(List.of(1L));
		given(postRepository.findAllByIdIn(anyList(), any())).willReturn(
			new PageImpl<>(List.of(getPost(postRequest()))));
		String typeInput = "content,title,skill";
		List<String> types = Arrays.stream(typeInput.split(",")).collect(Collectors.toList());

		Page<PostListResponse> searchResult = postService.search("content", types, PageRequest.of(0, 1));

		PostListResponse result = searchResult.getContent().get(0);
		assertEquals(objectMapper.writeValueAsString(result), objectMapper.writeValueAsString(postListResponse()));
	}

	@Test
	void searchByCategoryTest() throws Exception {
		given(postQueryRepository.findAllByQueryWithCategory(any(String.class), eq(Category.STUDY), anyList()))
			.willReturn(List.of(1L));
		given(postRepository.findAllByIdIn(anyList(), any())).willReturn(
			new PageImpl<>(List.of(getPost(postRequest()))));
		String typeInput = "content,title,skill";
		List<String> types = Arrays.stream(typeInput.split(",")).collect(Collectors.toList());

		Page<PostListResponse> searchResult = postService.searchByCategory("content", types, Category.STUDY,
			PageRequest.of(0, 1));

		PostListResponse result = searchResult.getContent().get(0);
		assertEquals(objectMapper.writeValueAsString(result), objectMapper.writeValueAsString(postListResponse()));
	}

	@Test
	void readPostsTest() throws Exception {
		//given
		given(postRepository.findAll(PageRequest.of(0, 1)))
			.willReturn(new PageImpl<>(List.of(getPost(postRequest()))));

		Page<PostListResponse> getPosts = postService.readPosts(PageRequest.of(0, 1));
		PostListResponse result = getPosts.getContent().get(0);

		assertEquals(objectMapper.writeValueAsString(result), objectMapper.writeValueAsString(postListResponse()));
	}

	@Test
	void readPostsByCategoryTest() throws Exception {
		given(postRepository.findAllByCategory(Category.STUDY, PageRequest.of(0, 1)))
			.willReturn(new PageImpl<>(List.of(getPost(postRequest()))));

		Page<PostListResponse> getPosts = postService.readPostsByCategory(Category.STUDY,
			PageRequest.of(0, 1));
		PostListResponse result = getPosts.getContent().get(0);

		assertEquals(objectMapper.writeValueAsString(result), objectMapper.writeValueAsString(postListResponse()));
	}
}