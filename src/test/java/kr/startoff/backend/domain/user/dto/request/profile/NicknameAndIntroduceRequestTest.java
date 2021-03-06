package kr.startoff.backend.domain.user.dto.request.profile;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class NicknameAndIntroduceRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void nicknameAndIntroduceUpdateRequestTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"nickname\":\"newNickname\",\n"
			+ "\"introduce\":\"Introduce\"\n"
			+ "}";
		//when
		NicknameAndIntroduceRequest updateRequest = objectMapper.readValue(requestBody,
			NicknameAndIntroduceRequest.class);
		//then
		assertEquals(updateRequest.getNickname(), nicknameAndIntroduceRequest().getNickname());
		assertEquals(updateRequest.getIntroduce(), nicknameAndIntroduceRequest().getIntroduce());
	}
}