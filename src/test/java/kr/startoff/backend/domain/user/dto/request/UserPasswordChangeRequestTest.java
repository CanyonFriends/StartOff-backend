package kr.startoff.backend.domain.user.dto.request;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class UserPasswordChangeRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void 비밀번호_변경요청_Dto_변환() throws Exception {
		UserPasswordChangeRequest userPasswordChangeRequest = userPasswordChangeRequest();
		String requestBody = "{\n"
			+ "\"beforePassword\":\"password\",\n"
			+ "\"afterPassword\":\"newPassword\"\n"
			+ "}";

		UserPasswordChangeRequest result = objectMapper.readValue(requestBody, UserPasswordChangeRequest.class);

		assertEquals(result.getBeforePassword(), userPasswordChangeRequest.getBeforePassword());
		assertEquals(result.getAfterPassword(), userPasswordChangeRequest.getAfterPassword());
	}
}