package kr.startoff.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.UserNotFoundException;
import kr.startoff.backend.model.request.SignupRequest;
import kr.startoff.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	@Transactional
	public User signUp(SignupRequest request) {
		User user = User.builder()
			.email(request.getEmail())
			.nickname(request.getNickname())
			.password(encoder.encode(request.getPassword())).build();

		return userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public boolean isDuplicateEmail(String email) {
		return userRepository.existsUserByEmail(email);
	}

	@Transactional(readOnly = true)
	public boolean isDuplicateNickname(String nickname) {
		return userRepository.existsUserByNickname(nickname);
	}
}
