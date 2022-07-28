package kr.startoff.backend.domain.user.dto.request;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.domain.Profile;

class ProfileRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void 프로필_변경요청_Dto_변환() throws Exception {
		ProfileRequest profileRequest = profileRequest();
		String requestBody = "{\n"
			+ "\"githubUrl\":\"https://github.com/protoseo\",\n"
			+ "\"blogUrl\":\"https://newBlogUrl.blog.com\",\n"
			+ "\"baekjoonId\":\"proto_type\",\n"
			+ "\"introduce\":\"Introduce\"\n"
			+ "}";

		ProfileRequest result = objectMapper.readValue(requestBody, ProfileRequest.class);

		assertEquals(profileRequest.getBaekjoonId(), result.getBaekjoonId());
		assertEquals(profileRequest.getGithubUrl(), result.getGithubUrl());
		assertEquals(profileRequest.getIntroduce(), result.getIntroduce());
		assertEquals(profileRequest.getBlogUrl(), result.getBlogUrl());
	}

	@Test
	void 프로필_변경요청_엔티티_변환() {
		Profile profile = profile();

		Profile result = profileRequest().toEntity();

		assertEquals(profile.getBaekjoonId(), result.getBaekjoonId());
		assertEquals(profile.getGithubUrl(), result.getGithubUrl());
		assertEquals(profile.getIntroduce(), result.getIntroduce());
		assertEquals(profile.getBlogUrl(), result.getBlogUrl());
	}
}