package kr.startoff.backend.domain.user.dto.request;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class LoginRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void loginRequestTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"email\":\"proto_seo@naver.com\",\n"
			+ "\"password\":\"Password\"\n"
			+ "}";
		//when
		LoginRequest loginRequest = objectMapper.readValue(requestBody,
			LoginRequest.class);
		//then
		assertEquals(loginRequest.getEmail(), loginRequest().getEmail());
		assertEquals(loginRequest.getPassword(), loginRequest().getPassword());
	}
}