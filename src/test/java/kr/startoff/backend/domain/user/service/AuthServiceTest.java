package kr.startoff.backend.domain.user.service;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.global.security.jwt.JwtUtil;
import kr.startoff.backend.global.util.RedisUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private RedisUtil redisUtil;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private UserDetailsService userDetailsService;

	@InjectMocks
	private AuthService authService;

	// TODO
	@Test
	void signUpTest() throws Exception {
		//given
		User user = user();
		given(userRepository.save(any())).willReturn(user);
		//when
		Long userId = authService.signup(signupRequest());
		//then

		assertNull(userId);
	}

}