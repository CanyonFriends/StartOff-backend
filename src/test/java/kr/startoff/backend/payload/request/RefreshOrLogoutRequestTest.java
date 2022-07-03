package kr.startoff.backend.payload.request;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.dto.request.RefreshOrLogoutRequest;

class RefreshOrLogoutRequestTest {
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
		RefreshOrLogoutRequest refreshOrLogoutRequest = objectMapper.readValue(requestBody,
			RefreshOrLogoutRequest.class);
		//then
		assertEquals(refreshOrLogoutRequest.getUuid(), refreshOrLogoutRequest().getUuid());
		assertEquals(refreshOrLogoutRequest.getEmail(), refreshOrLogoutRequest().getEmail());
		assertEquals(refreshOrLogoutRequest.getAccessToken(), refreshOrLogoutRequest().getAccessToken());
	}
}