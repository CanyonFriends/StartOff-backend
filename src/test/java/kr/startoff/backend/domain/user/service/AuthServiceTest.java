package kr.startoff.backend.domain.user.service;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;

import kr.startoff.backend.domain.user.domain.security.UserPrincipal;
import kr.startoff.backend.domain.user.dto.request.LoginRequest;
import kr.startoff.backend.domain.user.dto.request.RefreshRequest;
import kr.startoff.backend.domain.user.dto.request.SignupRequest;
import kr.startoff.backend.domain.user.dto.response.AccessTokenResponse;
import kr.startoff.backend.domain.user.dto.response.LoginResponse;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.global.common.dto.CommonResponse;
import kr.startoff.backend.global.security.jwt.JwtUtil;
import kr.startoff.backend.global.util.RedisUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	private final UserRepository userRepository = mock(UserRepository.class);
	private final JwtUtil jwtUtil = mock(JwtUtil.class);
	private final RedisUtil redisUtil = mock(RedisUtil.class);
	private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
	private final UserDetailsService userDetailsService = mock(UserDetailsService.class);

	@InjectMocks
	private AuthService authService;

	@Nested
	@DisplayName("회원가입")
	class SignupTest {
		@Test
		void 회원가입_성공_테스트() {
			given(userRepository.save(any())).willReturn(user());

			Long userId = authService.signup(signupRequest());

			assertEquals(USER_ID, userId);
		}

		@Test
		void 중복된_이메일_회원가입_실패_테스트() {
			SignupRequest request = signupRequest();
			given(userRepository.existsUserByEmail(EMAIL)).willReturn(true);

			assertThrows(UserException.class, () -> authService.signup(request));
		}

		@Test
		void 중복된_닉네임_회원가입_실패_테스트() {
			SignupRequest request = signupRequest();
			given(userRepository.existsUserByNickname(NICKNAME)).willReturn(true);

			assertThrows(UserException.class, () -> authService.signup(request));
		}
	}

	@Nested
	@DisplayName("로그인 테스트")
	class LoginTest {

		@Test
		void 로그인_성공() {
			given(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD))).willReturn(authenticationToken());
			given(jwtUtil.generateJwtToken(any(UserPrincipal.class))).willReturn(ACCESS_TOKEN);

			LoginResponse response = authService.login(loginRequest());

			assertEquals(loginResponse().getAccessToken(), response.getAccessToken());
			assertEquals(loginResponse().getEmail(), response.getEmail());
			assertEquals(loginResponse().getNickname(), response.getNickname());
			assertEquals(loginResponse().getUserId(), response.getUserId());
		}

		@Test
		void 비밀번호_틀림_로그인_실패() {
			given(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD))).willThrow(BadCredentialsException.class);
			LoginRequest request = loginRequest();

			assertThrows(UserException.class, () -> authService.login(request));
		}
	}

	@Nested
	@DisplayName("토큰 재발급")
	class RefreshTokenTest {
		@Test
		void 토큰_재발급_성공() {
			given(redisUtil.getData(USER_UUID)).willReturn(Optional.of(USER_ID.toString()));
			given(userDetailsService.loadUserByUsername(EMAIL)).willReturn(userPrincipal());
			given(jwtUtil.generateJwtToken(any(UserPrincipal.class))).willReturn(ACCESS_TOKEN);

			AccessTokenResponse response = authService.refreshToken(refreshRequest());

			assertEquals(accessTokenResponse().getUserId(), response.getUserId());
			assertEquals(accessTokenResponse().getAccessToken(), response.getAccessToken());
		}

		@Test
		void 토큰_재발급_실패() {
			given(redisUtil.getData(USER_UUID)).willReturn(Optional.empty());
			RefreshRequest request = refreshRequest();

			assertThrows(UserException.class, () -> authService.refreshToken(request));
		}
	}

	@Nested
	@DisplayName("로그아웃 테스트")
	class LogoutTest {
		@Test
		void 로그아웃_성공() {
			given(redisUtil.getData(USER_UUID)).willReturn(Optional.empty());

			CommonResponse response = authService.logout(logoutRequest());

			assertTrue(response.isSuccess());
			assertEquals("로그아웃", response.getMsg());
		}

		@Test
		void UUID_삭제_후_로그아웃_성공() {
			given(redisUtil.getData(USER_UUID)).willReturn(Optional.of(USER_ID.toString()));

			CommonResponse response = authService.logout(logoutRequest());

			assertTrue(response.isSuccess());
			assertEquals("로그아웃", response.getMsg());
		}
	}
}