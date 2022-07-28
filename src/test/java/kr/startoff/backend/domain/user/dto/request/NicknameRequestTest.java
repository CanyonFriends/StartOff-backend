package kr.startoff.backend.domain.user.dto.request;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class NicknameRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void 닉네임_변경요청_Dto_변환() throws Exception {
		NicknameRequest nicknameRequest = nicknameRequest();
		String requestBody = "{\n"
			+ "\"nickname\":\"nickname\"\n"
			+ "}";

		NicknameRequest result = objectMapper.readValue(requestBody, NicknameRequest.class);

		assertEquals(nicknameRequest.getNickname(), result.getNickname());
	}
}