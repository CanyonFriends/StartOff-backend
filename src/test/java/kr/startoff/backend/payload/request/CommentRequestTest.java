package kr.startoff.backend.payload.request;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class CommentRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void parentCommentRequestTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"user_id\":1,\n"
			+ "\"parent_id\":null,\n"
			+ "\"content\":\"Content\"\n"
			+ "}";
		//when
		CommentRequest parentCommentRequest = objectMapper.readValue(requestBody,
			CommentRequest.class);
		//then
		assertEquals(parentCommentRequest.getUserId(), parentCommentRequest().getUserId());
		assertEquals(parentCommentRequest.getParentId(), parentCommentRequest().getParentId());
		assertEquals(parentCommentRequest.getContent(), parentCommentRequest().getContent());
	}

	@Test
	void childCommentRequestTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"user_id\":1,\n"
			+ "\"parent_id\":1,\n"
			+ "\"content\":\"Content\"\n"
			+ "}";
		//when
		CommentRequest childCommentRequest = objectMapper.readValue(requestBody,
			CommentRequest.class);
		//then
		assertEquals(childCommentRequest.getUserId(), childCommentRequest().getUserId());
		assertEquals(childCommentRequest.getParentId(), childCommentRequest().getParentId());
		assertEquals(childCommentRequest.getContent(), childCommentRequest().getContent());
	}
}