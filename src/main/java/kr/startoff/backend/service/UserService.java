package kr.startoff.backend.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.custom.EmailOrNicknameDuplicateException;
import kr.startoff.backend.exception.custom.InvalidPasswordException;
import kr.startoff.backend.exception.custom.UserNotFoundException;
import kr.startoff.backend.payload.request.UserPasswordChangeRequest;
import kr.startoff.backend.payload.request.profile.BaekjoonIdRequest;
import kr.startoff.backend.payload.request.profile.BlogUrlRequest;
import kr.startoff.backend.payload.request.profile.GithubUrlRequest;
import kr.startoff.backend.payload.request.profile.NicknameAndIntroduceRequest;
import kr.startoff.backend.payload.response.UserInfoResponse;
import kr.startoff.backend.payload.response.UserProfileResponse;
import kr.startoff.backend.repository.UserRepository;
import kr.startoff.backend.util.S3UploadUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final S3UploadUtil s3UploadUtil;

	@Transactional(readOnly = true)
	public UserInfoResponse getUserInformation(Long id) {
		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		return new UserInfoResponse(user.getId(), user.getEmail(), user.getNickname());
	}

	@Transactional(readOnly = true)
	public UserProfileResponse getUserProfile(Long id) {
		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		return new UserProfileResponse(user);
	}

	@Transactional
	public boolean changeUserPassword(UserPasswordChangeRequest updateRequest, Long id) {
		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		if (!encoder.matches(updateRequest.getBeforePassword(), user.getPassword())) {
			throw new InvalidPasswordException();
		}
		user.setPassword(encoder.encode(updateRequest.getAfterPassword()));
		return true;
	}

	@Transactional
	public Long deleteUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		userRepository.delete(user);
		return id;
	}

	@Transactional
	public String updateNicknameAndIntroduce(Long id, NicknameAndIntroduceRequest nicknameRequest) {
		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		String updateNickname = nicknameRequest.getNickname();
		String updateIntroduce = nicknameRequest.getIntroduce();
		if (!user.getNickname().equals(updateNickname) && isDuplicateNickname(updateNickname)) {
			throw new EmailOrNicknameDuplicateException("Nickname이 중복되었습니다.");
		}
		user.setNickname(updateNickname);
		user.setIntroduce(updateIntroduce);
		return user.getNickname();
	}

	@Transactional
	public String updateGithubUrl(Long id, GithubUrlRequest githubUrlRequest) {
		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		String updateGithubUrl = githubUrlRequest.getGithubUrl();
		user.setGithubUrl(updateGithubUrl);
		return user.getGithubUrl();
	}

	@Transactional
	public String updateBlogUrl(Long id, BlogUrlRequest blogUrlRequest) {
		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		String updateBlogUrl = blogUrlRequest.getBlogUrl();
		user.setBlogUrl(updateBlogUrl);
		return user.getBlogUrl();
	}

	@Transactional
	public String updateBaekjoonId(Long id, BaekjoonIdRequest baekjoonIdRequest) {
		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		String updateBaekjoonId = baekjoonIdRequest.getBaekjoonId();
		user.setBaekjoonId(updateBaekjoonId);
		return user.getBaekjoonId();
	}

	@Transactional
	public String updateUserProfileImage(Long id, MultipartFile file) {
		User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
		String imageUrl = s3UploadUtil.uploadProfileImage(file, id);
		user.setImageUrl(imageUrl);
		return imageUrl;
	}

	@Transactional(readOnly = true)
	public boolean validateEmailOrNickname(String email, String nickname) {
		if (!email.equals("") && isDuplicateEmail(email)) {
			throw new EmailOrNicknameDuplicateException("Email이 중복되었습니다.");
		}
		if (!nickname.equals("") && isDuplicateNickname(nickname)) {
			throw new EmailOrNicknameDuplicateException("Nickname이 중복되었습니다.");
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
