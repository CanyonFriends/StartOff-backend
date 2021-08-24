package kr.startoff.backend.payload.response;

import static kr.startoff.backend.payload.PayloadFixture.*;
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
			+ "\"nickname\":\"newNickname\""
			+ "}";
		assertEquals(expect, objectMapper.writeValueAsString(userInfoResponse()));
	}
}