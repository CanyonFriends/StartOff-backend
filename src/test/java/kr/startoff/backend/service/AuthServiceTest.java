package kr.startoff.backend.service;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import kr.startoff.backend.entity.User;
import kr.startoff.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	private UserRepository userRepository = mock(UserRepository.class);
	private PasswordEncoder encoder = new BCryptPasswordEncoder();

	@InjectMocks
	private AuthService authService;

	// TODO
	@Test
	void signUpTest() throws Exception {
		//given
		User user = getUser(encoder);
		given(userRepository.save(any())).willReturn(user);
		//when
		Long userId = authService.signup(signupRequest());
		//then


		assertNull(userId);
	}

}