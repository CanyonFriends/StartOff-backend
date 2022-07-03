package kr.startoff.backend.payload.request;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.post.dto.PostRequest;

class PostRequestTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void postRequestTest() throws Exception {
		//given
		String requestBody = "{\n"
			+ "\"user_id\":1,\n"
			+ "\"title\":\"Title\",\n"
			+ "\"content\":\"Content\",\n"
			+ "\"category\":\"PROJECT\",\n"
			+ "\"post_skills\":[\"Spring Boot\",\"React\",\"Git\",\"AWS EC2\"],\n"
			+ "\"current_people\":1,\n"
			+ "\"max_people\":4\n"
			+ "}";

		//when
		PostRequest postRequest = objectMapper.readValue(requestBody,
			PostRequest.class);

		//then
		assertEquals(postRequest.getUserId(), postRequest().getUserId());
		assertEquals(postRequest.getTitle(), postRequest().getTitle());
		assertEquals(postRequest.getContent(), postRequest().getContent());
		assertEquals(postRequest.getCategory(), postRequest().getCategory());
		assertEquals(postRequest.getPostSkills(), postRequest().getPostSkills());
		assertEquals(postRequest.getCurrentPeople(), postRequest().getCurrentPeople());
		assertEquals(postRequest.getMaxPeople(), postRequest().getMaxPeople());
	}
}