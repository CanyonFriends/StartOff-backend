package kr.startoff.backend.domain.user.dto.request.profile;

import static org.junit.jupiter.api.Assertions.*;
import static kr.startoff.backend.payload.PayloadFixture.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class BlogUrlRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void blogUrlUpdateTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"blog_url\":\"https://newBlogUrl.blog.com\"\n"
			+ "}";
		//when
		BlogUrlRequest updateRequest = objectMapper.readValue(requestBody,
			BlogUrlRequest.class);
		//then
		assertEquals(updateRequest.getBlogUrl(), blogUrlRequest().getBlogUrl());
	}
}