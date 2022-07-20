package kr.startoff.backend.controller;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.global.config.SecurityConfig;
import kr.startoff.backend.domain.user.controller.AuthController;
import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.dto.request.LoginRequest;
import kr.startoff.backend.domain.user.dto.request.SignupRequest;
import kr.startoff.backend.global.exception.ExceptionType;
import kr.startoff.backend.domain.user.domain.security.UserPrincipal;
import kr.startoff.backend.global.security.jwt.JwtUtil;
import kr.startoff.backend.domain.user.service.AuthService;
import kr.startoff.backend.domain.user.service.UserDetailsServiceImpl;
import kr.startoff.backend.domain.user.service.UserService;
import kr.startoff.backend.global.util.RedisUtil;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	}
)
class AuthControllerTest {
	@MockBean
	private AuthenticationManager authenticationManager;
	@MockBean
	private UserService userService;
	@MockBean
	private AuthService authService;
	@MockBean
	private UserDetailsServiceImpl userDetailsService;
	@MockBean
	private JwtUtil jwtUtil;
	@MockBean
	private RedisUtil redisUtil;

	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	private void setUp(WebApplicationContext webApplicationContext) {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void signUpSuccessTest() throws Exception {
		SignupRequest request = signupRequest();
		User user = getUser();
		given(userService.validateEmailOrNickname(request.getEmail(), request.getNickname())).willReturn(true);
		given(authService.signup(any())).willReturn(USER_ID);

		String content = objectMapper.writeValueAsString(request);

		ResultActions resultActions = mockMvc.perform(post("/api/v1/signup")
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isCreated())
			.andExpect(content().string(Long.toString(user.getId())));
	}

	@Test
	void signUpThrowConflictEmailExceptionTest() throws Exception {
		SignupRequest request = signupRequest();
		given(authService.signup(any())).willThrow(
			new UserException(ExceptionType.DUPLICATE_EMAIL));

		String content = objectMapper.writeValueAsString(request);

		ResultActions resultActions = mockMvc.perform(post("/api/v1/signup")
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isConflict())
			.andExpect(jsonPath("error_msg").value("Email이 중복되었습니다."));
	}

	@Test
	void signUpThrowConflictNicknameExceptionTest() throws Exception {
		SignupRequest signupRequest = signupRequest();
		given(authService.signup(any())).willThrow(
			new UserException(ExceptionType.DUPLICATE_NICKNAME));

		String content = objectMapper.writeValueAsString(signupRequest);

		ResultActions resultActions = mockMvc.perform(post("/api/v1/signup")
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isConflict())
			.andExpect(jsonPath("error_msg").value("Nickname이 중복되었습니다."));
	}

	@Test
	void loginSuccessTest() throws Exception {
		LoginRequest loginRequest = loginRequest();
		UserPrincipal userPrincipal = UserPrincipal.create(getUser());
		UsernamePasswordAuthenticationToken token =
			new UsernamePasswordAuthenticationToken(userPrincipal, "password",
				List.of(new SimpleGrantedAuthority("ROLE_USER")));
		given(authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())))
			.willReturn(token);
		given(jwtUtil.generateJwtToken(any(UserPrincipal.class))).willReturn(ACCESS_TOKEN);
		given(jwtUtil.generateRefreshToken(any(UserPrincipal.class))).willReturn(REFRESH_TOKEN);

		mockMvc.perform(post("/api/v1/login")
				.content(objectMapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("access_token").value(ACCESS_TOKEN))
			.andExpect(jsonPath("user_id").value(USER_ID))
			.andExpect(jsonPath("email").value(EMAIL))
			.andExpect(jsonPath("nickname").value(NICKNAME))
			.andReturn();
	}

	@Test
	void loginThrowExceptionTest() throws Exception {
		LoginRequest loginRequest = loginRequest();
		given(authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())))
			.willThrow(BadCredentialsException.class);

		mockMvc.perform(post("/api/v1/login")
				.content(objectMapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("error_msg").value("비밀번호를 확인해주세요."))
			.andReturn();
	}

	@Test
	void refreshSuccessTest() throws Exception {
		UserPrincipal userPrincipal = UserPrincipal.create(getUser());
		given(redisUtil.getData(UUID)).willReturn(Optional.of(REFRESH_TOKEN));
		given(jwtUtil.validateJwtToken(REFRESH_TOKEN)).willReturn(true);
		given(jwtUtil.getUserNameFromJwtToken(REFRESH_TOKEN)).willReturn(EMAIL);
		given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(userPrincipal);
		given(jwtUtil.generateJwtToken(userPrincipal)).willReturn(NEW_ACCESS_TOKEN);

		mockMvc.perform(post("/api/v1/refresh")
				.content(objectMapper.writeValueAsString(refreshOrLogoutRequest()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("user_id").value(USER_ID))
			.andExpect(jsonPath("access_token").value(NEW_ACCESS_TOKEN))
			.andReturn();
	}

	@Test
	void refreshThrowExceptionTest1() throws Exception {
		given(redisUtil.getData(UUID)).willReturn(Optional.empty());

		mockMvc.perform(post("/api/v1/refresh")
				.content(objectMapper.writeValueAsString(refreshOrLogoutRequest()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andReturn();
	}

	@Test
	void refreshThrowExceptionTest2() throws Exception {
		given(redisUtil.getData(UUID)).willReturn(Optional.of(REFRESH_TOKEN));
		given(jwtUtil.validateJwtToken(REFRESH_TOKEN)).willReturn(false);

		mockMvc.perform(post("/api/v1/refresh")
				.content(objectMapper.writeValueAsString(refreshOrLogoutRequest()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andReturn();
	}

	@Test
	void refreshThrowExceptionTest3() throws Exception {
		given(redisUtil.getData(UUID)).willReturn(Optional.of(REFRESH_TOKEN));
		given(jwtUtil.validateJwtToken(REFRESH_TOKEN)).willReturn(true);
		given(jwtUtil.getUserNameFromJwtToken(REFRESH_TOKEN)).willReturn("NotInvalidEmail");

		mockMvc.perform(post("/api/v1/refresh")
				.content(objectMapper.writeValueAsString(refreshOrLogoutRequest()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andReturn();
	}

	@Test
	void logoutWithRedisHaveDataTest() throws Exception {
		given(redisUtil.getData(UUID)).willReturn(Optional.of(REFRESH_TOKEN));

		mockMvc.perform(post("/api/v1/logout")
				.content(objectMapper.writeValueAsString(refreshOrLogoutRequest()))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.accept(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isOk())
			.andExpect(jsonPath("success").value(true))
			.andExpect(jsonPath("msg").value("로그아웃"))
			.andReturn();
	}

	@Test
	void logoutWithRedisHaveNotDataTest() throws Exception {
		given(redisUtil.getData(UUID)).willReturn(Optional.empty());

		mockMvc.perform(post("/api/v1/logout")
				.content(objectMapper.writeValueAsString(refreshOrLogoutRequest()))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.accept(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isOk())
			.andExpect(jsonPath("success").value(true))
			.andExpect(jsonPath("msg").value("로그아웃"))
			.andReturn();
	}
}