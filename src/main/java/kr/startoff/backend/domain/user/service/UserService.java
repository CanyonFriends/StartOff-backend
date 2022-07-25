package kr.startoff.backend.domain.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.dto.request.NicknameRequest;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.user.dto.request.UserPasswordChangeRequest;
import kr.startoff.backend.domain.user.dto.response.UserInfoResponse;
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
		return UserInfoResponse.from(user);
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
	public String updateNickname(Long id, NicknameRequest nicknameRequest) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionType.USER_NOT_FOUND));
		String updateNickname = nicknameRequest.getNickname();
		if (isDuplicateNickname(updateNickname)) {
			throw new UserException(ExceptionType.DUPLICATE_NICKNAME);
		}
		user.updateNickname(updateNickname);
		return user.getNickname();
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
