package kr.startoff.backend.payload.response;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class PostResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void postResponseTest() throws Exception {
		final String expect = "{"
			+ "\"post_id\":1,"
			+ "\"user_id\":1,"
			+ "\"nickname\":\"Nickname\","
			+ "\"title\":\"Title\","
			+ "\"content\":\"Content\","
			+ "\"category\":\"PROJECT\","
			+ "\"max_people\":4,"
			+ "\"current_people\":1,"
			+ "\"post_skills\":"
			+ "[{\"skill_id\":1,\"skill_name\":\"Spring Boot\",\"color\":\"#000000\",\"text_color\":\"#FFFFFF\"},"
			+ "{\"skill_id\":2,\"skill_name\":\"React\",\"color\":\"#000000\",\"text_color\":\"#FFFFFF\"},"
			+ "{\"skill_id\":3,\"skill_name\":\"Git\",\"color\":\"#000000\",\"text_color\":\"#FFFFFF\"},"
			+ "{\"skill_id\":4,\"skill_name\":\"AWS EC2\",\"color\":\"#000000\",\"text_color\":\"#FFFFFF\"}],"
			+ "\"created_at\":\"2021-09-12T12:32:10\","
			+ "\"comments\":[]}";
		assertEquals(expect, objectMapper.writeValueAsString(postResponse()));
	}

	@Test
	void postResponseSetCommentResponseTest() throws Exception {
		final String expect = "{"
			+ "\"post_id\":1,"
			+ "\"user_id\":1,"
			+ "\"nickname\":\"Nickname\","
			+ "\"title\":\"Title\","
			+ "\"content\":\"Content\","
			+ "\"category\":\"PROJECT\","
			+ "\"max_people\":4,"
			+ "\"current_people\":1,"
			+ "\"post_skills\":"
			+ "[{\"skill_id\":1,\"skill_name\":\"Spring Boot\",\"color\":\"#000000\",\"text_color\":\"#FFFFFF\"},"
			+ "{\"skill_id\":2,\"skill_name\":\"React\",\"color\":\"#000000\",\"text_color\":\"#FFFFFF\"},"
			+ "{\"skill_id\":3,\"skill_name\":\"Git\",\"color\":\"#000000\",\"text_color\":\"#FFFFFF\"},"
			+ "{\"skill_id\":4,\"skill_name\":\"AWS EC2\",\"color\":\"#000000\",\"text_color\":\"#FFFFFF\"}],"
			+ "\"created_at\":\"2021-09-12T12:32:10\","
			+ "\"comments\":"
			+ "[{\"comment_id\":1,\"user_id\":1,"
			+ "\"nickname\":\"Nickname\","
			+ "\"content\":\"Content\","
			+ "\"created_at\":\"2021-09-12T12:32:10\","
			+ "\"is_deleted\":\"N\","
			+ "\"child_comment\":[{\"comment_id\":2,\"user_id\":1,\"nickname\":\"Nickname\",\"content\":\"Content\",\"created_at\":\"2021-09-12T12:32:10\",\"is_deleted\":\"N\",\"child_comment\":[]}]}]}";
		assertEquals(expect, objectMapper.writeValueAsString(postResponseSetComment()));
	}
}