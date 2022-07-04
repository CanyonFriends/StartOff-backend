package kr.startoff.backend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.global.config.SecurityConfig;
import kr.startoff.backend.domain.post.controller.PostController;
import kr.startoff.backend.domain.post.domain.Category;
import kr.startoff.backend.domain.post.dto.PostListResponse;
import kr.startoff.backend.domain.post.service.PostService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PostController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	}
)
class PostControllerTest {
	@MockBean
	private PostService postService;

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private final static String QUERY = "content";
	private final static String SEARCH_TYPE = "content,title,skill";
	@BeforeEach
	private void setUp(WebApplicationContext webApplicationContext) {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void savePostTest() throws Exception {
		given(postService.savePost(any())).willReturn(getPost(postRequest()));
		String content = objectMapper.writeValueAsString(postResponse());

		MvcResult result = mockMvc.perform(post("/api/v1/posts")
				.content(objectMapper.writeValueAsString(postRequest()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void updatePostTest() throws Exception {
		given(postService.updatePost(eq(POST_ID), any())).willReturn(updatePostResponse());
		String content = objectMapper.writeValueAsString(updatePostResponse());

		MvcResult result = mockMvc.perform(put("/api/v1/posts/{post_id}", POST_ID)
				.content(objectMapper.writeValueAsString(updatePostRequest()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void deletePostTest() throws Exception {
		given(postService.deletePost(POST_ID)).willReturn(POST_ID);
		String content = objectMapper.writeValueAsString(commonResponse("성공적으로 삭제되었습니다."));

		MvcResult result = mockMvc.perform(delete("/api/v1/posts/{post_id}", POST_ID)
				.contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void searchTest() throws Exception {
		Page<PostListResponse> content = new PageImpl<>(List.of(postListResponse()));
		given(postService.search(eq(QUERY), anyList(), any(PageRequest.class))).willReturn(content);

		MvcResult result = mockMvc.perform(get("/api/v1/posts", POST_ID)
				.queryParam("query", QUERY)
				.queryParam("type", SEARCH_TYPE)
				.queryParam("size", "1")
				.queryParam("page", "0"))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(objectMapper.writeValueAsString(content), result.getResponse().getContentAsString());
	}

	@Test
	void searchThrowSearchTypeExceptionTest() throws Exception {
		mockMvc.perform(get("/api/v1/posts", POST_ID)
				.queryParam("query", QUERY)
				.queryParam("type", "invalid")
				.queryParam("size", "1")
				.queryParam("page", "0"))
			.andExpect(status().isNotFound())
			.andReturn();
	}

	@Test
	void searchByCategoryTest() throws Exception {
		Page<PostListResponse> content = new PageImpl<>(List.of(postListResponse()));
		given(postService.searchByCategory(eq(QUERY), anyList(), any(Category.class), any(PageRequest.class)))
			.willReturn(content);

		MvcResult result = mockMvc.perform(get("/api/v1/posts", POST_ID)
				.queryParam("query", QUERY)
				.queryParam("type", SEARCH_TYPE)
				.queryParam("category", "Study")
				.queryParam("size", "1")
				.queryParam("page", "0"))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(objectMapper.writeValueAsString(content), result.getResponse().getContentAsString());
	}

	@Test
	void searchByCategoryThrowExceptionTest() throws Exception {
		mockMvc.perform(get("/api/v1/posts", POST_ID)
				.queryParam("query", QUERY)
				.queryParam("type", SEARCH_TYPE)
				.queryParam("category", "Hello")
				.queryParam("size", "1")
				.queryParam("page", "0"))
			.andExpect(status().isNotFound())
			.andReturn();
	}

	@Test
	void getPostsTest() throws Exception {
		Page<PostListResponse> content = new PageImpl<>(List.of(postListResponse()));
		given(postService.readPosts(any(PageRequest.class)))
			.willReturn(content);

		MvcResult result = mockMvc.perform(get("/api/v1/posts", POST_ID)
				.queryParam("size", "1")
				.queryParam("page", "0"))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(objectMapper.writeValueAsString(content), result.getResponse().getContentAsString());
	}

	@Test
	void getPostsByCategoryTest() throws Exception {
		Page<PostListResponse> content = new PageImpl<>(List.of(postListResponse()));
		given(postService.readPostsByCategory(any(Category.class), any(PageRequest.class)))
			.willReturn(content);

		MvcResult result = mockMvc.perform(get("/api/v1/posts", POST_ID)
				.queryParam("size", "1")
				.queryParam("page", "0")
				.queryParam("category", "Study"))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(objectMapper.writeValueAsString(content), result.getResponse().getContentAsString());
	}

	@Test
	void getPostsByCategoryThrowExceptionTest() throws Exception {
		mockMvc.perform(get("/api/v1/posts", POST_ID)
				.queryParam("size", "1")
				.queryParam("page", "0")
				.queryParam("category", "Study1"))
			.andExpect(status().isNotFound())
			.andReturn();
	}

	@Test
	void getPostTest() throws Exception {
		given(postService.readPost(POST_ID)).willReturn(postResponse());
		String content = objectMapper.writeValueAsString(postResponse());

		MvcResult result = mockMvc.perform(get("/api/v1/posts/{post_id}", POST_ID)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void getCategoriesTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/v1/categories")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		Class<Category> category = Category.class;
		Category[] categoryType = category.getEnumConstants();
		for (Category c : categoryType) {
			assertTrue(result.getResponse().getContentAsString().contains(c.toString().toLowerCase()));
		}
	}
}