package kr.startoff.backend.domain.user.service;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.domain.user.domain.security.UserPrincipal;

class UserDetailsServiceImplTest {
	private UserRepository userRepository;
	private UserDetailsServiceImpl userDetailsService;
	@BeforeEach
	private void setUp() {
		userRepository = mock(UserRepository.class);
		userDetailsService = new UserDetailsServiceImpl(userRepository);
	}

	@Test
	void loadUserByUsernameTest() throws Exception {
		User user = user();
		SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_USER");
		given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(user));

		UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(EMAIL);

		assertEquals(user.getNickname(), userPrincipal.getNickname());
		assertEquals(user.getEmail(), userPrincipal.getEmail());
		assertTrue(userPrincipal.getAuthorities().contains(role));
	}
}