package kr.startoff.backend.domain.user.dto.request;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class LogoutRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void 로그아웃요청_Dto_변환() throws Exception {
		LogoutRequest logoutRequest = logoutRequest();
		String requestBody = "{\n"
			+ "\"accessToken\":\"accessToken\",\n"
			+ "\"uuid\":\"" + USER_UUID + "\"\n"
			+ "}";

		LogoutRequest result = objectMapper.readValue(requestBody, LogoutRequest.class);

		assertEquals(logoutRequest.getAccessToken(), result.getAccessToken());
		assertEquals(logoutRequest.getUuid(), result.getUuid());
	}
}