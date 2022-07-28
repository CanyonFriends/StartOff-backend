package kr.startoff.backend.domain.user.controller;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static kr.startoff.backend.global.exception.ExceptionType.*;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.dto.request.LogoutRequest;
import kr.startoff.backend.domain.user.dto.request.RefreshRequest;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.global.common.dto.CommonResponse;
import kr.startoff.backend.global.config.SecurityConfig;
import kr.startoff.backend.domain.user.dto.request.LoginRequest;
import kr.startoff.backend.domain.user.dto.request.SignupRequest;
import kr.startoff.backend.domain.user.service.AuthService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	}
)
class AuthControllerTest {
	@MockBean
	private AuthService authService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Nested
	@DisplayName("회원가입 테스트")
	class SignupTest {
		private final SignupRequest signupRequest = signupRequest();

		@Test
		void 회원가입_성공() throws Exception {
			given(authService.signup(any(SignupRequest.class))).willReturn(USER_ID);
			String content = objectMapper.writeValueAsString(signupRequest);

			ResultActions resultActions = mockMvc.perform(post("/api/v1/signup")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

			resultActions.andExpect(status().isCreated())
				.andExpect(content().string(Long.toString(USER_ID)));
		}

		@Test
		void 중복된_이메일_회원가입_실패() throws Exception {
			given(authService.signup(any(SignupRequest.class))).willThrow(new UserException(DUPLICATE_EMAIL));
			String content = objectMapper.writeValueAsString(signupRequest);

			ResultActions resultActions = mockMvc.perform(post("/api/v1/signup")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

			resultActions.andExpect(status().isConflict())
				.andExpect(jsonPath("message").value(DUPLICATE_EMAIL.getMessage()));
		}

		@Test
		void 중복된_닉네임_회원가입_실패() throws Exception {
			given(authService.signup(any(SignupRequest.class))).willThrow(new UserException(DUPLICATE_NICKNAME));
			String content = objectMapper.writeValueAsString(signupRequest);

			ResultActions resultActions = mockMvc.perform(post("/api/v1/signup")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

			resultActions.andExpect(status().isConflict())
				.andExpect(jsonPath("message").value(DUPLICATE_NICKNAME.getMessage()));
		}
	}

	@Nested
	@DisplayName("로그인 테스트")
	class LoginTest {
		private final LoginRequest loginRequest = loginRequest();

		@Test
		void 로그인_성공() throws Exception {
			given(authService.login(any(LoginRequest.class))).willReturn(loginResponse());

			ResultActions resultActions = mockMvc.perform(post("/api/v1/login")
				.content(objectMapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

			resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("accessToken").value(ACCESS_TOKEN))
				.andExpect(jsonPath("userId").value(USER_ID))
				.andExpect(jsonPath("email").value(EMAIL))
				.andExpect(jsonPath("nickname").value(NICKNAME))
				.andExpect(jsonPath("uuid").value(USER_UUID))
				.andReturn();
		}

		@Test
		void 비밀번호_로그인_실패() throws Exception {
			given(authService.login(any(LoginRequest.class))).willThrow(new UserException(INVALID_PASSWORD));

			ResultActions resultActions = mockMvc.perform(post("/api/v1/login")
				.content(objectMapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

			resultActions.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("message").value(INVALID_PASSWORD.getMessage()))
				.andReturn();
		}
	}

	@Nested
	@DisplayName("토큰 재발급 테스트")
	class RefreshTokenTest {
		private final RefreshRequest request = refreshRequest();

		@Test
		void 토큰_재발급_성공() throws Exception {
			given(authService.refreshToken(any(RefreshRequest.class))).willReturn(accessTokenResponse());

			ResultActions resultActions = mockMvc.perform(post("/api/v1/refresh")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

			resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("userId").value(USER_ID))
				.andExpect(jsonPath("accessToken").value(ACCESS_TOKEN))
				.andReturn();
		}

		@Test
		void 토큰_재발급_실패() throws Exception {
			given(authService.refreshToken(any(RefreshRequest.class))).willThrow(
				new UserException(REFRESH_TOKEN_EXPIRED));

			ResultActions resultActions = mockMvc.perform(post("/api/v1/refresh")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

			resultActions.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("message").value(REFRESH_TOKEN_EXPIRED.getMessage()))
				.andReturn();
		}
	}

	@Nested
	@DisplayName("로그아웃 테스트")
	class LogoutTest {
		private final LogoutRequest request = logoutRequest();

		@Test
		void 로그아웃_성공() throws Exception {
			given(authService.logout(any(LogoutRequest.class))).willReturn(new CommonResponse(true, "로그아웃"));

			ResultActions resultActions = mockMvc.perform(post("/api/v1/logout")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

			resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("success").value(true))
				.andExpect(jsonPath("msg").value("로그아웃"))
				.andReturn();
		}
	}
}