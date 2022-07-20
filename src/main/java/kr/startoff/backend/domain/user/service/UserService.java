package kr.startoff.backend.domain.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.user.dto.request.UserPasswordChangeRequest;
import kr.startoff.backend.domain.user.dto.request.profile.BaekjoonIdRequest;
import kr.startoff.backend.domain.user.dto.request.profile.BlogUrlRequest;
import kr.startoff.backend.domain.user.dto.request.profile.GithubUrlRequest;
import kr.startoff.backend.domain.user.dto.request.profile.NicknameAndIntroduceRequest;
import kr.startoff.backend.domain.user.dto.response.UserInfoResponse;
import kr.startoff.backend.domain.user.dto.response.UserProfileResponse;
import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.global.exception.ExceptionType;
import kr.startoff.backend.global.util.S3UploadUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final S3UploadUtil s3UploadUtil;

	@Transactional(readOnly = true)
	public UserInfoResponse getUserInformation(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionType.USER_NOT_FOUND));
		return new UserInfoResponse(user.getId(), user.getEmail(), user.getNickname());
	}

	@Transactional(readOnly = true)
	public UserProfileResponse getUserProfile(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionType.USER_NOT_FOUND));
		return new UserProfileResponse(user);
	}

	@Transactional
	public boolean changeUserPassword(UserPasswordChangeRequest updateRequest, Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionType.USER_NOT_FOUND));
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		if (!encoder.matches(updateRequest.getBeforePassword(), user.getPassword())) {
			throw new UserException(ExceptionType.INVALID_PASSWORD);
		}
		user.updatePassword(encoder.encode(updateRequest.getAfterPassword()));
		return true;
	}

	@Transactional
	public Long deleteUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionType.USER_NOT_FOUND));
		userRepository.delete(user);
		return id;
	}

	@Transactional
	public String updateNicknameAndIntroduce(Long id, NicknameAndIntroduceRequest nicknameRequest) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionType.USER_NOT_FOUND));
		String updateNickname = nicknameRequest.getNickname();
		String updateIntroduce = nicknameRequest.getIntroduce();
		if (!user.getNickname().equals(updateNickname) && isDuplicateNickname(updateNickname)) {
			throw new UserException(ExceptionType.DUPLICATE_NICKNAME);
		}
		user.updateNickname(updateNickname);
		user.updateIntroduce(updateIntroduce);
		return user.getNickname();
	}

	@Transactional
	public String updateGithubUrl(Long id, GithubUrlRequest githubUrlRequest) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionType.USER_NOT_FOUND));
		String updateGithubUrl = githubUrlRequest.getGithubUrl();
		user.updateGithubUrl(updateGithubUrl);
		return user.getGithubUrl();
	}

	@Transactional
	public String updateBlogUrl(Long id, BlogUrlRequest blogUrlRequest) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionType.USER_NOT_FOUND));
		String updateBlogUrl = blogUrlRequest.getBlogUrl();
		user.updateBlogUrl(updateBlogUrl);
		return user.getBlogUrl();
	}

	@Transactional
	public String updateBaekjoonId(Long id, BaekjoonIdRequest baekjoonIdRequest) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionType.USER_NOT_FOUND));
		String updateBaekjoonId = baekjoonIdRequest.getBaekjoonId();
		user.updateBaekjoonId(updateBaekjoonId);
		return user.getBaekjoonId();
	}

	@Transactional
	public String updateUserProfileImage(Long id, MultipartFile file) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionType.USER_NOT_FOUND));
		String imageUrl = s3UploadUtil.uploadProfileImage(file, id);
		user.updateImageUrl(imageUrl);
		return imageUrl;
	}

	@Transactional(readOnly = true)
	public boolean validateEmailOrNickname(String email, String nickname) {
		if (!email.equals("") && isDuplicateEmail(email)) {
			throw new UserException(ExceptionType.DUPLICATE_EMAIL);
		}
		if (!nickname.equals("") && isDuplicateNickname(nickname)) {
			throw new UserException(ExceptionType.DUPLICATE_NICKNAME);
		}
		return email.equals("") != nickname.equals("");
	}

	private boolean isDuplicateEmail(String email) {
		return userRepository.existsUserByEmail(email);
	}

	private boolean isDuplicateNickname(String nickname) {
		return userRepository.existsUserByNickname(nickname);
	}
}
