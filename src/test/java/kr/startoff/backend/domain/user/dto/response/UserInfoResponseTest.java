package kr.startoff.backend.domain.user.dto.response;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class UserInfoResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void userInfoResponseTest() throws Exception {
		final String expect = "{"
			+ "\"user_id\":1,"
			+ "\"email\":\"proto_seo@naver.com\","
			+ "\"nickname\":\"Nickname\""
			+ "}";
		assertEquals(expect, objectMapper.writeValueAsString(userInfoResponse()));
	}
}