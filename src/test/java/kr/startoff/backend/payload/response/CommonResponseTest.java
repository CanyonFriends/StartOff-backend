package kr.startoff.backend.payload.response;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class CommonResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void commonResponseTest() throws Exception{
		final String expect = "{"
			+ "\"success\":true,"
			+ "\"msg\":\"성공하였습니다.\"}";
		assertEquals(expect,objectMapper.writeValueAsString(commonResponse("성공하였습니다.")));
	}
}