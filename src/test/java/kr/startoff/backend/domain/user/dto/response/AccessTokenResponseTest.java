package kr.startoff.backend.domain.user.dto.response;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;


class AccessTokenResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void accessTokenResponseTest() throws Exception{
	    final String expect = "{"
			+ "\"user_id\":1,"
			+ "\"access_token\":\"access token\"}";
	    assertEquals(expect,objectMapper.writeValueAsString(accessTokenResponse()));
	}
}