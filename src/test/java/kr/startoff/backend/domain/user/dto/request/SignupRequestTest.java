package kr.startoff.backend.payload.request;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.dto.request.SignupRequest;

class SignupRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void signupRequestTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"email\":\"proto_seo@naver.com\",\n"
			+ "\"password\":\"Password\",\n"
			+ "\"nickname\":\"Nickname\"\n"
			+ "}";
		//when
		SignupRequest signupRequest = objectMapper.readValue(requestBody,
			SignupRequest.class);
		//then
		assertEquals(signupRequest.getEmail(), signupRequest().getEmail());
		assertEquals(signupRequest.getPassword(), signupRequest().getPassword());
		assertEquals(signupRequest.getNickname(), signupRequest().getNickname());
	}
}