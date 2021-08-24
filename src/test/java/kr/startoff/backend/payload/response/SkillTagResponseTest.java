package kr.startoff.backend.payload.response;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class SkillTagResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void skillTagResponseTest() throws Exception {
		final String expect = "{"
			+ "\"skill_id\":1,"
			+ "\"skill_name\":\"Spring Boot\","
			+ "\"color\":\"#000000\","
			+ "\"text_color\":\"#FFFFFF\""
			+ "}";
		assertEquals(expect, objectMapper.writeValueAsString(skillTagResponse()));
	}
}