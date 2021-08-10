package kr.startoff.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.entity.AuthProvider;
import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.UserNotFoundException;
import kr.startoff.backend.model.request.SignupRequest;
import kr.startoff.backend.model.request.UserInfoUpdateRequest;
import kr.startoff.backend.model.response.UserInfoResponse;
import kr.startoff.backend.model.response.UserProfileResponse;
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
			.password(encoder.encode(request.getPassword()))
			.provider(AuthProvider.local)
			.build();

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

	@Transactional(readOnly = true)
	public UserInfoResponse getUserInformation(Long id) {
		User user = getUserOrThrowException(id);
		return UserInfoResponse.builder()
			.email(user.getEmail())
			.nickname(user.getNickname())
			.build();
	}

	@Transactional(readOnly = true)
	public UserProfileResponse getUserProfile(Long id) {
		User user = getUserOrThrowException(id);
		return UserProfileResponse.builder()
			.nickname(user.getNickname())
			.introduce(user.getIntroduce())
			.githubUrl(user.getGithubUrl())
			.blogUrl(user.getBlogUrl())
			.baekjoonId(user.getBaekjoonId())
			.build();
	}

	@Transactional
	public UserInfoResponse updateUser(UserInfoUpdateRequest updateRequest, Long id) {
		User user = getUserOrThrowException(id);
		user.setNickname(updateRequest.getNickname());
		user.setPassword(encoder.encode(updateRequest.getPassword()));
		return UserInfoResponse.builder()
			.email(user.getEmail())
			.nickname(user.getNickname())
			.build();
	}

	@Transactional
	public Long deleteUser(Long id) {
		User user = getUserOrThrowException(id);
		userRepository.delete(user);
		return id;
	}

	private User getUserOrThrowException(Long id) {
		return userRepository.findById(id).orElseThrow(
			() -> new UserNotFoundException("해당 ID를 가진 회원이 존재하지 않습니다.")
		);
	}
}
