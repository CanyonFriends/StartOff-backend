package kr.startoff.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.entity.AuthProvider;
import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.custom.EmailOrNicknameDuplicateException;
import kr.startoff.backend.exception.custom.InvalidPasswordException;
import kr.startoff.backend.exception.custom.UserNotFoundException;
import kr.startoff.backend.payload.request.SignupRequest;
import kr.startoff.backend.payload.request.UserPasswordChangeRequest;
import kr.startoff.backend.payload.request.profile.BaekjoonIdRequest;
import kr.startoff.backend.payload.request.profile.BlogUrlRequest;
import kr.startoff.backend.payload.request.profile.GithubUrlRequest;
import kr.startoff.backend.payload.request.profile.IntroduceRequest;
import kr.startoff.backend.payload.request.profile.NicknameRequest;
import kr.startoff.backend.payload.response.UserInfoResponse;
import kr.startoff.backend.payload.response.UserProfileResponse;
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
		return new UserInfoResponse(user.getId(), user.getEmail(), user.getNickname());
	}

	@Transactional(readOnly = true)
	public UserProfileResponse getUserProfile(Long id) {
		User user = getUserOrThrowException(id);
		return new UserProfileResponse(user);
	}

	@Transactional
	public boolean changeUserPassword(UserPasswordChangeRequest updateRequest, Long id) {
		User user = getUserOrThrowException(id);
		if (!encoder.matches(updateRequest.getBeforePassword(), user.getPassword())) {
			throw new InvalidPasswordException();
		}
		user.setPassword(encoder.encode(updateRequest.getAfterPassword()));
		return true;
	}

	@Transactional
	public Long deleteUser(Long id) {
		User user = getUserOrThrowException(id);
		userRepository.delete(user);
		return id;
	}

	@Transactional
	public String updateNickname(Long id, NicknameRequest nicknameRequest) {
		User user = getUserOrThrowException(id);
		String updateNickname = nicknameRequest.getNickname();
		if (userRepository.existsUserByNickname(updateNickname)) {
			throw new EmailOrNicknameDuplicateException("Nickname이 중복되었습니다.");
		}
		user.setNickname(updateNickname);
		return user.getNickname();
	}

	@Transactional
	public String updateGithubUrl(Long id, GithubUrlRequest githubUrlRequest) {
		User user = getUserOrThrowException(id);
		String updateGithubUrl = githubUrlRequest.getGithubUrl();
		user.setGithubUrl(updateGithubUrl);
		return user.getGithubUrl();
	}

	@Transactional
	public String updateBlogUrl(Long id, BlogUrlRequest blogUrlRequest) {
		User user = getUserOrThrowException(id);
		String updateBlogUrl = blogUrlRequest.getBlogUrl();
		user.setBlogUrl(updateBlogUrl);
		return user.getBlogUrl();
	}

	@Transactional
	public String updateIntroduce(Long id, IntroduceRequest introduceRequest) {
		User user = getUserOrThrowException(id);
		String updateIntroduce = introduceRequest.getIntroduce();
		user.setIntroduce(updateIntroduce);
		return user.getIntroduce();
	}

	@Transactional
	public String updateBaekjoonId(Long id, BaekjoonIdRequest baekjoonIdRequest) {
		User user = getUserOrThrowException(id);
		String updateBaekjoonId = baekjoonIdRequest.getBaekjoonId();
		user.setBaekjoonId(updateBaekjoonId);
		return user.getBaekjoonId();
	}

	private User getUserOrThrowException(Long id) {
		return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
	}
}
