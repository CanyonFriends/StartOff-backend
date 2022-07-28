package kr.startoff.backend.domain.user.dto.response;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class AccessTokenResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final AccessTokenResponse accessTokenResponse = accessTokenResponse();

	@Test
	void 직렬화_테스트() throws Exception {
		final String expect = "{"
			+ "\"userId\":1,"
			+ "\"accessToken\":\"accessToken\"}";
		assertEquals(expect, objectMapper.writeValueAsString(accessTokenResponse));
	}

	@Test
	void 변환_테스트() {
		AccessTokenResponse result = AccessTokenResponse.of(USER_ID, ACCESS_TOKEN);

		assertEquals(accessTokenResponse.getUserId(), result.getUserId());
		assertEquals(accessTokenResponse.getAccessToken(), result.getAccessToken());
	}
}