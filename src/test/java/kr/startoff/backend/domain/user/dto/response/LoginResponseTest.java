package kr.startoff.backend.domain.user.dto.response;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class LoginResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final LoginResponse loginResponse = loginResponse();

	@Test
	void 직렬화_테스트() throws Exception {
		final String expect = "{"
			+ "\"userId\":1,"
			+ "\"email\":\"proto_seo@naver.com\","
			+ "\"nickname\":\"nickname\","
			+ "\"accessToken\":\"accessToken\","
			+ "\"uuid\":\"" + USER_UUID + "\"}";

		assertEquals(expect, objectMapper.writeValueAsString(loginResponse));
	}

	@Test
	void 변환_테스트() {
		LoginResponse result = LoginResponse.of(userPrincipal(), ACCESS_TOKEN, USER_UUID);

		assertEquals(loginResponse.getNickname(), result.getNickname());
		assertEquals(loginResponse.getEmail(), result.getEmail());
		assertEquals(loginResponse.getUserId(), result.getUserId());
		assertEquals(loginResponse.getAccessToken(), result.getAccessToken());
		assertEquals(loginResponse.getUuid(), result.getUuid());
	}
}