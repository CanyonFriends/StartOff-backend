package kr.startoff.backend.domain.user.dto.response;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class UserInfoResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final UserInfoResponse userInfoResponse = userInfoResponse();

	@Test
	void 직렬화_테스트() throws Exception {
		final String expect = "{"
			+ "\"userId\":1,"
			+ "\"email\":\"proto_seo@naver.com\","
			+ "\"nickname\":\"nickname\""
			+ "}";
		assertEquals(expect, objectMapper.writeValueAsString(userInfoResponse));
	}

	@Test
	void User_변환_테스트() {
		UserInfoResponse result = UserInfoResponse.from(user());

		assertEquals(userInfoResponse.getUserId(), result.getUserId());
		assertEquals(userInfoResponse.getNickname(), result.getNickname());
		assertEquals(userInfoResponse.getEmail(), result.getEmail());
	}

	@Test
	void UserPrincipal_변환_테스트() {
		UserInfoResponse result = UserInfoResponse.from(userPrincipal());

		assertEquals(userInfoResponse.getUserId(), result.getUserId());
		assertEquals(userInfoResponse.getNickname(), result.getNickname());
		assertEquals(userInfoResponse.getEmail(), result.getEmail());

	}
}