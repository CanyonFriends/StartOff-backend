package kr.startoff.backend.controller;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.config.SecurityConfig;
import kr.startoff.backend.payload.request.profile.BaekjoonIdRequest;
import kr.startoff.backend.payload.request.profile.BlogUrlRequest;
import kr.startoff.backend.payload.request.profile.GithubUrlRequest;
import kr.startoff.backend.payload.request.profile.NicknameAndIntroduceRequest;
import kr.startoff.backend.payload.response.CommonResponse;
import kr.startoff.backend.service.SkillTagService;
import kr.startoff.backend.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProfileController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	}
)
class ProfileControllerTest {
	@MockBean
	private UserService userService;
	@MockBean
	private SkillTagService skillTagService;
	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	private void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void getUserProfileTest() throws Exception {
		given(userService.getUserProfile(USER_ID)).willReturn(userProfileResponse());
		String content = objectMapper.writeValueAsString(userProfileResponse());

		MvcResult result = mockMvc.perform(get("/api/v1/users/{user_id}/profile", USER_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void updateUserNicknameAndIntroduceTest() throws Exception {
		given(userService.updateNicknameAndIntroduce(eq(USER_ID), any(NicknameAndIntroduceRequest.class)))
			.willReturn(NEW_NICKNAME);
		Map<String, String> contentMap = new HashMap<>();
		contentMap.put("nickname", NEW_NICKNAME);
		contentMap.put("introduce", INTRODUCE);
		String content = objectMapper.writeValueAsString(contentMap);

		MvcResult result = mockMvc.perform(put("/api/v1/users/{user_id}/introduce", USER_ID)
				.content(objectMapper.writeValueAsString(nicknameAndIntroduceRequest()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void githubUrlUpdateTest() throws Exception {
		given(userService.updateGithubUrl(eq(USER_ID), any(GithubUrlRequest.class)))
			.willReturn(NEW_GITHUB_URL);
		Map<String, String> contentMap = new HashMap<>();
		contentMap.put("github_url", NEW_GITHUB_URL);
		String content = objectMapper.writeValueAsString(contentMap);

		MvcResult result = mockMvc.perform(put("/api/v1/users/{user_id}/github-url", USER_ID)
				.content(objectMapper.writeValueAsString(githubUrlRequest()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void blogUrlUpdateTest() throws Exception {
		given(userService.updateBlogUrl(eq(USER_ID), any(BlogUrlRequest.class)))
			.willReturn(NEW_BLOG_URL);
		Map<String, String> contentMap = new HashMap<>();
		contentMap.put("blog_url", NEW_BLOG_URL);
		String content = objectMapper.writeValueAsString(contentMap);

		MvcResult result = mockMvc.perform(put("/api/v1/users/{user_id}/blog-url", USER_ID)
				.content(objectMapper.writeValueAsString(blogUrlRequest()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void baekjoonIdUpdateTest() throws Exception {
		given(userService.updateBaekjoonId(eq(USER_ID), any(BaekjoonIdRequest.class)))
			.willReturn(BAEKJOON_ID);
		Map<String, String> contentMap = new HashMap<>();
		contentMap.put("baekjoon_id", BAEKJOON_ID);
		String content = objectMapper.writeValueAsString(contentMap);

		MvcResult result = mockMvc.perform(put("/api/v1/users/{user_id}/baekjoon-id", USER_ID)
				.content(objectMapper.writeValueAsString(baekjoonIdRequest()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void userSkillAddTest() throws Exception {
		given(skillTagService.addSkillTagToUser(USER_ID, SKILL_NAME))
			.willReturn(skillTagResponse());
		String content = objectMapper.writeValueAsString(skillTagResponse());

		MvcResult result = mockMvc.perform(put("/api/v1/users/{user_id}/skills", USER_ID)
				.content(objectMapper.writeValueAsString(skillTagRequest()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void userSkillDeleteTest() throws Exception {
		CommonResponse response = new CommonResponse(true, "해당 기술태그가 삭제되었습니다.");
		String content = objectMapper.writeValueAsString(response);

		MvcResult result = mockMvc.perform(delete("/api/v1/users/{user_id}/skills/{skill_id}", USER_ID, SKILL_ID)
				.content(objectMapper.writeValueAsString(response))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.accept(MediaType.APPLICATION_JSON_UTF8))
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

}