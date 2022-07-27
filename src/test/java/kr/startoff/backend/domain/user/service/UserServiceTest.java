package kr.startoff.backend.domain.user.service;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.util.Base64;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.dto.request.NicknameRequest;
import kr.startoff.backend.domain.user.dto.request.UserPasswordChangeRequest;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.user.dto.response.UserInfoResponse;
import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.global.exception.custom.ImageUploadFailureException;
import kr.startoff.backend.global.util.S3UploadUtil;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	private final UserRepository userRepository = mock(UserRepository.class);
	private final S3UploadUtil s3UploadUtil = mock(S3UploadUtil.class);
	private final PasswordEncoder encoder = mock(PasswordEncoder.class);

	@InjectMocks
	private UserService userService;

	@Test
	void 유저_기본정보_가져오기_성공() {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user()));

		//when
		UserInfoResponse userInfoResponse = userService.getUserInformation(USER_ID);

		//then
		assertNotNull(userInfoResponse);
		assertEquals(userInfoResponse.getUserId(), user().getId());
		assertEquals(userInfoResponse.getEmail(), user().getEmail());
		assertEquals(userInfoResponse.getNickname(), user().getNickname());
	}

	@Test
	void 비밀번호_변경_성공() {
		User user = user();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		given(encoder.matches(PASSWORD, user.getPassword())).willReturn(true);

		boolean result = userService.changeUserPassword(userPasswordChangeRequest(), USER_ID);

		assertTrue(result);
	}

	@Test
	void 비밀번호_변경_실패() {
		User user = user();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		given(encoder.matches(PASSWORD, user.getPassword())).willReturn(false);
		UserPasswordChangeRequest request = userPasswordChangeRequest();

		assertThrows(UserException.class, () -> userService.changeUserPassword(request, USER_ID));
	}

	@Test
	void 회원탈퇴_실패() {
		when(userRepository.findById(USER_ID)).thenThrow(UserException.class);

		assertThrows(UserException.class, () -> userService.deleteUser(USER_ID));
	}

	@Test
	void 회원탈퇴_성공() {
		User user = user();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		Long result = userService.deleteUser(USER_ID);

		verify(userRepository, times(1)).delete(user);
		assertEquals(USER_ID, result);
	}

	@Test
	void 닉네임_변경_성공() {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user()));
		given(userRepository.existsUserByNickname(NICKNAME)).willReturn(false);

		String updateNickname = userService.updateNickname(USER_ID, nicknameRequest());

		assertEquals(NICKNAME, updateNickname);
	}

	@Test
	void 중복된_닉네임_변경_실패() {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user()));
		given(userRepository.existsUserByNickname(NICKNAME)).willReturn(true);
		NicknameRequest request = nicknameRequest();

		assertThrows(UserException.class, () -> userService.updateNickname(USER_ID, request));
	}

	@Test
	void 유저_이미지_변경_성공() {
		MockMultipartFile multipartFile = new MockMultipartFile("image", "test.png",
			MediaType.IMAGE_PNG_VALUE, Base64.getEncoder().encode("image".getBytes()));
		User user = user();
		String newImageUrl = String.format("https://bucket.s3.ap-northeast-2.amazonaws.com/%d/profile.png", USER_ID);
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		given(s3UploadUtil.uploadProfileImage(any(), eq(USER_ID))).willReturn(newImageUrl);

		String result = userService.updateUserProfileImage(USER_ID, multipartFile);

		assertEquals(newImageUrl, user.getImageUrl());
		assertEquals(newImageUrl, result);
	}

	@Test
	void 유저_이미지_변경_실패() {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user()));
		given(s3UploadUtil.uploadProfileImage(any(), eq(USER_ID))).willThrow(ImageUploadFailureException.class);

		assertThrows(ImageUploadFailureException.class,
			() -> userService.updateUserProfileImage(USER_ID, null), "업로드를 실패 하였습니다.");
	}

	@Nested
	@DisplayName("이메일과 닉네임 검사")
	class EmailAndNicknameValidationTest {

		@Test
		void 닉네임_중복검사_성공() {
			given(userRepository.existsUserByNickname(NICKNAME)).willReturn(false);

			boolean result = userService.validateEmailOrNickname("", NICKNAME);

			assertTrue(result);
		}

		@Test
		void 닉네임_중복검사_실패() {
			given(userRepository.existsUserByNickname(NICKNAME)).willReturn(true);

			assertThrows(UserException.class, () -> userService.validateEmailOrNickname("", NICKNAME));
		}

		@Test
		void 이메일_중복검사_성공() {
			given(userRepository.existsUserByEmail(EMAIL)).willReturn(false);

			boolean result = userService.validateEmailOrNickname(EMAIL, "");

			assertTrue(result);
		}

		@Test
		void 이메일_중복검사_실패() {
			given(userRepository.existsUserByEmail(EMAIL)).willReturn(true);

			assertThrows(UserException.class, () -> userService.validateEmailOrNickname(EMAIL, ""));
		}

		@Test
		void 이메일_닉네임_중복검사_동시_요청_실패() {
			given(userRepository.existsUserByNickname(NICKNAME)).willReturn(false);
			given(userRepository.existsUserByEmail(EMAIL)).willReturn(false);

			boolean result = userService.validateEmailOrNickname(EMAIL, NICKNAME);

			assertFalse(result);
		}

		@Test
		void 이메일_닉네임_중복검사_값이_없는경우() {
			boolean result = userService.validateEmailOrNickname("", "");

			assertFalse(result);
		}
	}
}