package kr.startoff.backend.domain.user.service;

import static kr.startoff.backend.global.exception.ExceptionType.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.global.security.UserPrincipal;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByEmail(username)
			.orElseThrow(() -> new UserException(USER_NOT_FOUND));
		return UserPrincipal.create(user);
	}
}
