package kr.startoff.backend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.config.SecurityConfig;
import kr.startoff.backend.service.CommentService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CommentController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	}
)
class CommentControllerTest {
	@MockBean
	private CommentService commentService;
	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	private void setUp(WebApplicationContext webApplicationContext) {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void saveCommentTest() throws Exception {
		given(commentService.saveComment(eq(POST_ID), any())).willReturn(parentCommentResponse());
		String content = objectMapper.writeValueAsString(parentCommentResponse());

		MvcResult result = mockMvc.perform(post("/api/v1/posts/{post_id}/comments", POST_ID)
				.content(objectMapper.writeValueAsString(parentCommentRequest()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void updateCommentTest() throws Exception {
		given(commentService.updateComment(eq(PARENT_ID), any())).willReturn(updateCommentResponse());
		String content = objectMapper.writeValueAsString(updateCommentResponse());

		MvcResult result = mockMvc.perform(put("/api/v1/posts/{post_id}/comments/{comment_id}", POST_ID, PARENT_ID)
				.content(objectMapper.writeValueAsString(updateCommentRequest()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void deleteCommentTest() throws Exception {
		given(commentService.deleteComment(PARENT_ID)).willReturn(PARENT_ID);

		MvcResult result = mockMvc.perform(delete("/api/v1/posts/{post_id}/comments/{comment_id}", POST_ID, PARENT_ID)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		assertTrue(result.getResponse().getContentAsString().contains(PARENT_ID.toString()));
	}
}