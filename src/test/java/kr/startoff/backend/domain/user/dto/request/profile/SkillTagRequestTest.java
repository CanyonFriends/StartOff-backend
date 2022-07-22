package kr.startoff.backend.domain.user.dto.request.profile;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.dto.request.SkillTagRequest;

class SkillTagRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void skillTagAddRequestTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"skill_name\":\"Spring Boot\"\n"
			+ "}";
		//when
		SkillTagRequest updateRequest = objectMapper.readValue(requestBody,
			SkillTagRequest.class);
		//then
		assertEquals(updateRequest.getSkillName(), skillTagRequest().getSkillName());
	}
}