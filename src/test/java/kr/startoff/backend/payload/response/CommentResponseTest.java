package kr.startoff.backend.payload.response;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class CommentResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void parentCommentResponseTest() throws Exception {
		final String expect = "{\"comment_id\":1,\"user_id\":1,"
			+ "\"nickname\":\"Nickname\",\"content\":\"Content\",\"created_at\":\"2021-09-12T12:32:10\",\"is_deleted\":\"N\","
			+ "\"child_comment\":"
			+ "[{\"comment_id\":2,\"user_id\":1,\"nickname\":\"Nickname\",\"content\":\"Content\",\"created_at\":\"2021-09-12T12:32:10\",\"is_deleted\":\"N\",\"child_comment\":[]}"
			+ "]}";
		assertEquals(expect, objectMapper.writeValueAsString(parentCommentResponse(childCommentResponse())));
	}

	@Test
	void childCommentResponseTest() throws Exception {
		final String expect = "{\"comment_id\":2,\"user_id\":1,"
			+ "\"nickname\":\"Nickname\",\"content\":\"Content\",\"created_at\":\"2021-09-12T12:32:10\",\"is_deleted\":\"N\",\"child_comment\":[]}";
		assertEquals(expect, objectMapper.writeValueAsString(childCommentResponse()));
	}
}