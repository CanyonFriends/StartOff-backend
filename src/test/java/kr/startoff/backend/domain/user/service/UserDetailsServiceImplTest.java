package kr.startoff.backend.domain.user.service;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.domain.user.domain.security.UserPrincipal;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
	private final UserRepository userRepository = mock(UserRepository.class);

	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;

	@Test
	void 이메일을_통해서_UserPrincipal_조회_성공() {
		User user = user();
		SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_USER");
		given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(user));

		UserPrincipal userPrincipal = (UserPrincipal)userDetailsService.loadUserByUsername(EMAIL);

		assertEquals(user.getNickname(), userPrincipal.getNickname());
		assertEquals(user.getEmail(), userPrincipal.getEmail());
		assertTrue(userPrincipal.getAuthorities().contains(role));
	}

	@Test
	void 이메일을_통해서_UserPrincipal_조회_실패() {
		given(userRepository.findByEmail(EMAIL)).willThrow(UserException.class);

		assertThrows(UserException.class, () -> userDetailsService.loadUserByUsername(EMAIL));
	}
}