package kr.startoff.backend.domain.user.service;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.dto.request.ProfileRequest;
import kr.startoff.backend.domain.user.dto.response.UserProfileResponse;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
	private final UserRepository userRepository = mock(UserRepository.class);

	@InjectMocks
	private ProfileService profileService;

	@Test
	void 프로필_불러오기_성공() {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user()));

		UserProfileResponse result = profileService.getUserProfile(USER_ID);

		assertEquals(userProfileResponse().getGithubUrl(), result.getGithubUrl());
		assertEquals(userProfileResponse().getBlogUrl(), result.getBlogUrl());
		assertEquals(userProfileResponse().getIntroduce(), result.getIntroduce());
		assertEquals(userProfileResponse().getBaekjoonId(), result.getBaekjoonId());
	}

	@Test
	void 프로필_불러오기_실패() {
		given(userRepository.findById(USER_ID)).willThrow(UserException.class);

		assertThrows(UserException.class, () -> profileService.getUserProfile(USER_ID));
	}

	@Test
	void 프로필_업데이트_성공() {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user()));
		ProfileRequest updateRequest = new ProfileRequest(NEW_GITHUB_URL, NEW_BLOG_URL, NEW_BAEKJOON_ID, NEW_INTRODUCE);

		UserProfileResponse result = profileService.updateProfile(USER_ID, updateRequest);

		assertEquals(NEW_GITHUB_URL, result.getGithubUrl());
		assertEquals(NEW_BLOG_URL, result.getBlogUrl());
		assertEquals(NEW_INTRODUCE, result.getIntroduce());
		assertEquals(NEW_BAEKJOON_ID, result.getBaekjoonId());
	}

	@Test
	void 프로필_업데이트_실패() {
		given(userRepository.findById(USER_ID)).willThrow(UserException.class);

		assertThrows(UserException.class, () -> profileService.getUserProfile(USER_ID));
	}
}