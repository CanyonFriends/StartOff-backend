package kr.startoff.backend.domain.user.dto.response;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class LoginResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void loginResponseTest() throws Exception{
		final String expect = "{"
			+ "\"access_token\":\"access token\","
			+ "\"uuid\":\"uuid\",\"user_id\":1,"
			+ "\"email\":\"proto_seo@naver.com\","
			+ "\"nickname\":\"Nickname\","
			+ "\"token_type\":\"Bearer\"}";

		assertEquals(expect,objectMapper.writeValueAsString(loginResponse()));
	}
}