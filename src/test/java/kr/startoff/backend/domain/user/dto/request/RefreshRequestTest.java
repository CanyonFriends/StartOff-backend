package kr.startoff.backend.payload.request;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.dto.request.RefreshRequest;

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
		assertEquals(refreshRequest.getUuid(), refreshOrLogoutRequest().getUuid());
		assertEquals(refreshRequest.getEmail(), refreshOrLogoutRequest().getEmail());
		assertEquals(refreshRequest.getAccessToken(), refreshOrLogoutRequest().getAccessToken());
	}
}