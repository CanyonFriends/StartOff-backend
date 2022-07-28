package kr.startoff.backend.domain.user.controller;

import static kr.startoff.backend.global.exception.ExceptionType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Base64;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.startoff.backend.common.annotation.WithUserPrincipal;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.global.config.SecurityConfig;
import kr.startoff.backend.domain.user.dto.request.UserPasswordChangeRequest;
import kr.startoff.backend.domain.user.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	}
)
class UserControllerTest {
	@MockBean
	private UserService userService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Nested
	@DisplayName("이메일, 닉네임 중복검사 테스트")
	class EmailAndNicknameValidationTest {
		@Test
		void 이메일_닉네임_둘다_입력이_없는_경우_실패() throws Exception {
			given(userService.validateEmailOrNickname("", "")).willReturn(false);

			mockMvc.perform(get("/api/v1/users/validation"))
				.andExpect(status().isBadRequest());
		}

		@Test
		void 이메일_닉네임_둘다_입력이_있는_경우_실패() throws Exception {
			given(userService.validateEmailOrNickname(EMAIL, NICKNAME)).willReturn(false);

			mockMvc.perform(get("/api/v1/users/validation")
					.queryParam("nickname", NICKNAME)
					.queryParam("email", EMAIL))
				.andExpect(status().isBadRequest());
		}

		@Test
		void 이메일_중복검사_성공() throws Exception {
			given(userService.validateEmailOrNickname(EMAIL, "")).willReturn(true);

			ResultActions resultActions = mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("email", EMAIL));

			resultActions.andExpect(status().isNoContent());
		}

		@Test
		void 닉네임_중복검사_성공() throws Exception {
			given(userService.validateEmailOrNickname("", NICKNAME)).willReturn(true);

			ResultActions resultActions = mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("nickname", NICKNAME));

			resultActions.andExpect(status().isNoContent());
		}

		@Test
		void 이메일_중복검사_실패() throws Exception {
			given(userService.validateEmailOrNickname(EMAIL, "")).willThrow(new UserException(DUPLICATE_EMAIL));

			ResultActions resultActions = mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("email", EMAIL));

			resultActions.andExpect(status().isConflict());
		}

		@Test
		void 닉네임_중복검사_실패() throws Exception {
			given(userService.validateEmailOrNickname("", NICKNAME)).willThrow(new UserException(DUPLICATE_NICKNAME));

			ResultActions resultActions = mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("nickname", NICKNAME));

			resultActions.andExpect(status().isConflict());
		}
	}

	@Test
	void 비밀번호_변경_성공() throws Exception {
		given(userService.changeUserPassword(any(UserPasswordChangeRequest.class), eq(USER_ID))).willReturn(true);

		ResultActions resultActions = mockMvc.perform(put("/api/v1/users/{userId}/password", USER_ID)
			.content(objectMapper.writeValueAsString(userPasswordChangeRequest()))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("success").value(true))
			.andExpect(jsonPath("msg").value("비밀번호가 변경되었습니다."));
	}

	@Test
	void 회원탈퇴_성공() throws Exception {
		given(userService.deleteUser(USER_ID)).willReturn(USER_ID);

		ResultActions resultActions = mockMvc.perform(delete("/api/v1/users/{userId}", USER_ID));

		resultActions.andExpect(status().isOk())
			.andReturn();
	}

	@Test
	void 프로필_이미지_변경_성공() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("image", "test.jpeg",
			MediaType.IMAGE_JPEG_VALUE, Base64.getEncoder().encode("image".getBytes()));
		given(userService.updateUserProfileImage(eq(USER_ID), any())).willReturn(IMAGE_URL);

		ResultActions resultActions = mockMvc.perform(multipart("/api/v1/users/{userId}/image", USER_ID)
			.file(multipartFile)
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.accept(MediaType.MULTIPART_FORM_DATA_VALUE));

		resultActions.andExpect(content().string(IMAGE_URL));
	}

	@Test
	@WithUserPrincipal
	void 로그인_유저_정보반환_성공() throws Exception {
		String content = objectMapper.writeValueAsString(userInfoResponse());

		MvcResult result = mockMvc.perform(get("/api/v1/users/self")).andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}
}