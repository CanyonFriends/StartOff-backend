package kr.startoff.backend.payload.request.profile;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class GithubUrlRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void githubUrlUpdateTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"github_url\":\"https://github.com/protoseo\"\n"
			+ "}";
		//when
		GithubUrlRequest updateRequest = objectMapper.readValue(requestBody,
			GithubUrlRequest.class);
		//then
		assertEquals(updateRequest.getGithubUrl(), githubUrlRequest().getGithubUrl());
	}
}