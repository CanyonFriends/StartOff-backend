package kr.startoff.backend.domain.user.dto.request;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class RefreshRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void 토큰요청_Dto_변환() throws Exception {
		RefreshRequest refreshRequest = refreshRequest();
		String requestBody = "{\n"
			+ "\"uuid\":\"" + USER_UUID + "\",\n"
			+ "\"email\":\"proto_seo@naver.com\",\n"
			+ "\"accessToken\":\"accessToken\"\n"
			+ "}";

		RefreshRequest result = objectMapper.readValue(requestBody, RefreshRequest.class);

		assertEquals(refreshRequest.getUuid(), result.getUuid());
		assertEquals(refreshRequest.getEmail(), result.getEmail());
		assertEquals(refreshRequest.getAccessToken(), result.getAccessToken());
	}
}