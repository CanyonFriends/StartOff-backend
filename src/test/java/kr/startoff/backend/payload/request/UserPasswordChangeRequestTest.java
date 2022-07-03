package kr.startoff.backend.payload.request;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.dto.request.UserPasswordChangeRequest;

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
		assertEquals(userPasswordChangeRequest.getBeforePassword(), passwordChangeRequest().getBeforePassword());
		assertEquals(userPasswordChangeRequest.getAfterPassword(), passwordChangeRequest().getAfterPassword());
	}
}