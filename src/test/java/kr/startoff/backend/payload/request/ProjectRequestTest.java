package kr.startoff.backend.payload.request;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class ProjectRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void projectAddRequestTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"title\":\"Title\",\n"
			+ "\"introduce\":\"Introduce\",\n"
			+ "\"content\":\"Content\",\n"
			+ "\"github_url\":\"https://github.com/Start-Off/StartOff-backend\",\n"
			+ "\"deploy_url\":\"https://startoff.kr\",\n"
			+ "\"start_date\":\"2021-07-26\",\n"
			+ "\"end_date\":\"2021-09-30\",\n"
			+ "\"project_skills\":[\"Spring Boot\",\"React\",\"Git\",\"AWS EC2\"]\n"
			+ "}";
		//when
		ProjectRequest projectRequest = objectMapper.readValue(requestBody,
			ProjectRequest.class);
		//then
		assertEquals(projectRequest.getTitle(), projectRequest().getTitle());
		assertEquals(projectRequest.getIntroduce(), projectRequest().getIntroduce());
		assertEquals(projectRequest.getContent(), projectRequest().getContent());
		assertEquals(projectRequest.getGithubUrl(), projectRequest().getGithubUrl());
		assertEquals(projectRequest.getDeployUrl(), projectRequest().getDeployUrl());
		assertEquals(projectRequest.getStartDate(), projectRequest().getStartDate());
		assertEquals(projectRequest.getEndDate(), projectRequest().getEndDate());
		assertEquals(projectRequest.getProjectSkills(), projectRequest().getProjectSkills());
	}
}