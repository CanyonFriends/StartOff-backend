package kr.startoff.backend.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.dto.request.ProfileRequest;
import kr.startoff.backend.domain.user.dto.response.UserProfileResponse;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.global.exception.ExceptionType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public UserProfileResponse getUserProfile(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionType.USER_NOT_FOUND));
		return UserProfileResponse.from(user.getProfile());
	}

	@Transactional
	public UserProfileResponse updateProfile(Long id, ProfileRequest profileRequest) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserException(ExceptionType.USER_NOT_FOUND));
		user.updateProfile(profileRequest.toEntity());
		return UserProfileResponse.from(user.getProfile());
	}
}