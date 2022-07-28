package kr.startoff.backend.domain.user.dto.request;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.domain.User;

class SignupRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void 회원가입요청_Dto_변환() throws Exception {
		SignupRequest signupRequest = signupRequest();
		String requestBody = "{\n"
			+ "\"email\":\"proto_seo@naver.com\",\n"
			+ "\"password\":\"password\",\n"
			+ "\"nickname\":\"nickname\"\n"
			+ "}";

		SignupRequest result = objectMapper.readValue(requestBody, SignupRequest.class);

		assertEquals(signupRequest.getEmail(), result.getEmail());
		assertEquals(signupRequest.getPassword(), result.getPassword());
		assertEquals(signupRequest.getNickname(), result.getNickname());
	}

	@Test
	void 회원가입요청_엔티티_변환() {
		User user = user();

		User result = signupRequest().toEntity();

		assertEquals(user.getEmail(), result.getEmail());
		assertTrue(new BCryptPasswordEncoder().matches(user.getPassword(), result.getPassword()));
		assertEquals(user.getNickname(), result.getNickname());
		assertEquals(user.getProvider(), result.getProvider());
	}
}