package kr.startoff.backend.domain.user.controller;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.service.ProfileService;
import kr.startoff.backend.global.config.SecurityConfig;
import kr.startoff.backend.global.common.dto.CommonResponse;
import kr.startoff.backend.domain.tag.service.SkillTagService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProfileController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	}
)
class ProfileControllerTest {
	@MockBean
	private ProfileService profileService;
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
		given(profileService.getUserProfile(USER_ID)).willReturn(userProfileResponse());
		String content = objectMapper.writeValueAsString(userProfileResponse());

		MvcResult result = mockMvc.perform(get("/api/v1/users/{user_id}/profile", USER_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}
}