package kr.startoff.backend.domain.user.dto.response;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class UserProfileResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	UserProfileResponse userProfileResponse = userProfileResponse();

	@Test
	void 직렬화_테스트() throws Exception {
		final String expect = "{"
			+ "\"introduce\":\"Introduce\","
			+ "\"githubUrl\":\"https://github.com/protoseo\","
			+ "\"blogUrl\":\"https://newBlogUrl.blog.com\","
			+ "\"baekjoonId\":\"proto_type\""
			+ "}";
		assertEquals(expect, objectMapper.writeValueAsString(userProfileResponse));
	}

	@Test
	void 변환_테스트() {
		UserProfileResponse result = UserProfileResponse.from(profile());

		assertEquals(userProfileResponse.getBaekjoonId(), result.getBaekjoonId());
		assertEquals(userProfileResponse.getIntroduce(), result.getIntroduce());
		assertEquals(userProfileResponse.getBlogUrl(), result.getBlogUrl());
		assertEquals(userProfileResponse.getGithubUrl(), result.getGithubUrl());
	}
}