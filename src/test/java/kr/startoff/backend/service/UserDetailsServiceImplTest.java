package kr.startoff.backend.service;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.domain.user.service.UserDetailsServiceImpl;
import kr.startoff.backend.global.security.UserPrincipal;

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
		User user = getUser();
		SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_USER");
		given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(user));

		UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(EMAIL);

		assertEquals(user.getNickname(), userPrincipal.getNickname());
		assertEquals(user.getEmail(), userPrincipal.getEmail());
		assertTrue(userPrincipal.getAuthorities().contains(role));
	}
}