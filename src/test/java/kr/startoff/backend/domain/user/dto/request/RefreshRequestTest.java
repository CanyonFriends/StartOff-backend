package kr.startoff.backend.domain.user.dto.request;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class RefreshRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void refreshOrLogoutRequestTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"uuid\":\"uuid\",\n"
			+ "\"email\":\"proto_seo@naver.com\",\n"
			+ "\"access_token\":\"access token\"\n"
			+ "}";
		//when
		RefreshRequest refreshRequest = objectMapper.readValue(requestBody,
			RefreshRequest.class);
		//then
		assertEquals(refreshRequest.getUuid(), refreshRequest().getUuid());
		assertEquals(refreshRequest.getEmail(), refreshRequest().getEmail());
		assertEquals(refreshRequest.getAccessToken(), refreshRequest().getAccessToken());
	}
}