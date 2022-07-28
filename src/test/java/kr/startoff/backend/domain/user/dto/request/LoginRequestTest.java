package kr.startoff.backend.domain.user.dto.request;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class LoginRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void 로그인요청_Dto_변환() throws Exception {
		LoginRequest loginRequest = loginRequest();
		String requestBody = "{\n"
			+ "\"email\":\"proto_seo@naver.com\",\n"
			+ "\"password\":\"password\"\n"
			+ "}";

		LoginRequest result = objectMapper.readValue(requestBody, LoginRequest.class);

		assertEquals(loginRequest.getEmail(), result.getEmail());
		assertEquals(loginRequest.getPassword(), result.getPassword());
	}
}