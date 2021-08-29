package kr.startoff.backend.payload.request.profile;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class BaekjoonIdRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void baekjoonIdUpdateTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"baekjoon_id\":\"proto_type\"\n"
			+ "}";
		//when
		BaekjoonIdRequest updateRequest = objectMapper.readValue(requestBody,
			BaekjoonIdRequest.class);
		//then
		assertEquals(updateRequest.getBaekjoonId(), baekjoonIdRequest().getBaekjoonId());
	}
}