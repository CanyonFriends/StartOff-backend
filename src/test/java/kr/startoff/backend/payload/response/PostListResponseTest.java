package kr.startoff.backend.payload.response;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class PostListResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void postListResponseTest() throws Exception {
		final String expect = "{"
			+ "\"post_id\":1,"
			+ "\"nickname\":\"Nickname\","
			+ "\"title\":\"Title\","
			+ "\"max_people\":4,"
			+ "\"current_people\":1,"
			+ "\"post_skills\":"
			+ "[{\"skill_id\":1,\"skill_name\":\"Spring Boot\",\"color\":\"#000000\",\"text_color\":\"#FFFFFF\"},"
			+ "{\"skill_id\":2,\"skill_name\":\"React\",\"color\":\"#000000\",\"text_color\":\"#FFFFFF\"},"
			+ "{\"skill_id\":3,\"skill_name\":\"Git\",\"color\":\"#000000\",\"text_color\":\"#FFFFFF\"},"
			+ "{\"skill_id\":4,\"skill_name\":\"AWS EC2\",\"color\":\"#000000\",\"text_color\":\"#FFFFFF\"}],"
			+ "\"created_at\":\"2021-09-12T12:32:10\"}";
		assertEquals(expect, objectMapper.writeValueAsString(postListResponse()));
	}
}