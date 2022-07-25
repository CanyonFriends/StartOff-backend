package kr.startoff.backend.domain.user.dto.request;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class UserPasswordChangeRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void userPasswordChangeRequestTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"before_password\":\"Password\",\n"
			+ "\"after_password\":\"NEW_PASSWORD\"\n"
			+ "}";
		//when
		UserPasswordChangeRequest userPasswordChangeRequest = objectMapper.readValue(requestBody,
			UserPasswordChangeRequest.class);
		//then
		assertEquals(userPasswordChangeRequest.getBeforePassword(), userPasswordChangeRequest().getBeforePassword());
		assertEquals(userPasswordChangeRequest.getAfterPassword(), userPasswordChangeRequest().getAfterPassword());
	}
}