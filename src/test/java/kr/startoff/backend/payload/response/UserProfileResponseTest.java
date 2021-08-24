package kr.startoff.backend.payload.response;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class UserProfileResponseTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void userProfileResponseTest() throws Exception {
		final String expect = "{"
			+ "\"nickname\":\"newNickname\","
			+ "\"introduce\":\"안녕하세요. 반갑습니다.\","
			+ "\"github_url\":\"https://github.com/protoseo\","
			+ "\"blog_url\":\"https://newBlogUrl.blog.com\","
			+ "\"baekjoon_id\":\"proto_type\","
			+ "\"projects\":"
			+ "[{\"id\":1,"
			+ "\"title\":\"Project Title\","
			+ "\"introduce\":\"Project Introduce\","
			+ "\"content\":\"Project Content\","
			+ "\"github_url\":\"https://github.com/Start-Off/StartOff-backend\","
			+ "\"deploy_url\":\"https://startoff.kr\","
			+ "\"start_date\":\"2021-07-26\","
			+ "\"end_date\":\"2021-09-30\","
			+ "\"project_skills\":"
			+ "[{\"skill_id\":1,"
			+ "\"skill_name\":\"Spring Boot\","
			+ "\"color\":\"#000000\","
			+ "\"text_color\":\"#FFFFFF\"}]"
			+ "}],"
			+ "\"user_skills\":"
			+ "[{\"skill_id\":1,"
			+ "\"skill_name\":\"Spring Boot\","
			+ "\"color\":\"#000000\","
			+ "\"text_color\":\"#FFFFFF\"}]"
			+ "}";
		System.out.println(userProfileResponse());
		assertEquals(expect, objectMapper.writeValueAsString(userProfileResponse()));
	}
}