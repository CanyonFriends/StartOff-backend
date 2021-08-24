package kr.startoff.backend.payload.response;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class ProjectResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void projectResponseTest() throws Exception {
		final String expect = "{"
			+ "\"id\":1,"
			+ "\"title\":\"Project Title\","
			+ "\"introduce\":\"Project Introduce\","
			+ "\"content\":\"Project Content\","
			+ "\"github_url\":\"https://github.com/Start-Off/StartOff-backend\","
			+ "\"deploy_url\":\"https://startoff.kr\","
			+ "\"start_date\":\"2021-07-26\","
			+ "\"end_date\":\"2021-09-30\","
			+ "\"project_skills\":"
			+ "[{\"skill_id\":1,"
			+ "\"skill_name\":\"Spring Boot\","
			+ "\"color\":\"#000000\","
			+ "\"text_color\":\"#FFFFFF\""
			+ "}]"
			+ "}";
		assertEquals(expect, objectMapper.writeValueAsString(projectResponse()));
	}
}