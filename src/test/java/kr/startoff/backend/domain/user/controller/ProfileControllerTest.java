package kr.startoff.backend.domain.user.controller;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static kr.startoff.backend.global.exception.ExceptionType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.dto.request.ProfileRequest;
import kr.startoff.backend.domain.user.dto.response.UserProfileResponse;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.user.service.ProfileService;
import kr.startoff.backend.global.config.SecurityConfig;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProfileController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	}
)
class ProfileControllerTest {
	@MockBean
	private ProfileService profileService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Nested
	@DisplayName("프로필 테스트")
	class ProfileTest {
		private final ProfileRequest request = profileRequest();

		@Test
		void 프로필_가져오기_성공() throws Exception {
			UserProfileResponse userProfileResponse = userProfileResponse();
			given(profileService.getUserProfile(USER_ID)).willReturn(userProfileResponse);

			ResultActions resultActions = mockMvc.perform(get("/api/v1/users/{userId}/profile", USER_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

			resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("githubUrl").value(GITHUB_URL))
				.andExpect(jsonPath("introduce").value(INTRODUCE))
				.andExpect(jsonPath("blogUrl").value(BLOG_URL))
				.andExpect(jsonPath("baekjoonId").value(BAEKJOON_ID));
		}

		@Test
		void 프로필_가져오기_실패() throws Exception {
			given(profileService.getUserProfile(USER_ID)).willThrow(new UserException(USER_NOT_FOUND));

			ResultActions resultActions = mockMvc.perform(get("/api/v1/users/{userId}/profile", USER_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

			resultActions.andExpect(status().isNotFound())
				.andExpect(jsonPath("message").value(USER_NOT_FOUND.getMessage()));
		}

		@Test
		void 프로필_수정하기_성공() throws Exception {
			given(profileService.updateProfile(eq(USER_ID), any(ProfileRequest.class)))
				.willReturn(userProfileResponse());
			String content = objectMapper.writeValueAsString(request);

			ResultActions resultActions = mockMvc.perform(put("/api/v1/users/{userId}/profile", USER_ID)
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

			resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("githubUrl").value(GITHUB_URL))
				.andExpect(jsonPath("introduce").value(INTRODUCE))
				.andExpect(jsonPath("blogUrl").value(BLOG_URL))
				.andExpect(jsonPath("baekjoonId").value(BAEKJOON_ID));
		}

		@Test
		void 프로필_수정하기_실패() throws Exception {
			given(profileService.updateProfile(eq(USER_ID), any(ProfileRequest.class)))
				.willThrow(new UserException(USER_NOT_FOUND));
			String content = objectMapper.writeValueAsString(request);

			ResultActions resultActions = mockMvc.perform(put("/api/v1/users/{userId}/profile", USER_ID)
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

			resultActions.andExpect(status().isNotFound())
				.andExpect(jsonPath("message").value(USER_NOT_FOUND.getMessage()));
		}
	}

}