package kr.startoff.backend.domain.user.service;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.util.Base64;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.global.exception.custom.ImageUploadFailureException;
import kr.startoff.backend.domain.user.dto.response.UserInfoResponse;
import kr.startoff.backend.domain.user.dto.response.UserProfileResponse;
import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.global.util.S3UploadUtil;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	private UserRepository userRepository;
	private PasswordEncoder encoder;
	private UserService userService;
	private S3UploadUtil s3UploadUtil;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	private void setUp() {
		userRepository = mock(UserRepository.class);
		s3UploadUtil = mock(S3UploadUtil.class);
		encoder = new BCryptPasswordEncoder();
		userService = new UserService(userRepository, s3UploadUtil);
	}

	@Test
	void changeUserPasswordTest() throws Exception {
		//given
		User user = getUser(encoder);
		String beforePassword = user.getPassword();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		//when
		userService.changeUserPassword(passwordChangeRequest(), USER_ID);
		Optional<User> changePasswordUser = userRepository.findById(USER_ID);

		//then
		assertNotNull(changePasswordUser);
		assertTrue(changePasswordUser.isPresent());
		assertNotEquals(changePasswordUser.get().getPassword(), beforePassword);
	}

	@Test
	void changeUserPasswordThrowInvalidPasswordExceptionTest() throws Exception {
		User user = getUser(encoder);
		user.setPassword(encoder.encode(NEW_PASSWORD));
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		assertThrows(InvalidPasswordException.class,
			() -> userService.changeUserPassword(passwordChangeRequest(), USER_ID),
			"비밀번호를 확인해주세요.");
	}

	@Test
	void getUserInformationTest() throws Exception {
		//given
		User user = getUser(encoder);
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		//when
		UserInfoResponse userInfoResponse = userService.getUserInformation(USER_ID);

		//then
		assertNotNull(userInfoResponse);
		assertEquals(userInfoResponse.getNickname(), user.getNickname());
		assertEquals(userInfoResponse.getEmail(), user.getEmail());
	}

	@Test
	void deleteUserThrowExceptionTest() throws Exception {
		when(userRepository.findById(USER_ID)).thenThrow(UserNotFoundException.class);

		assertThrows(UserNotFoundException.class,
			() -> userService.deleteUser(USER_ID),
			"해당 유저를 찾을 수 없습니다.");
	}

	@Test
	void deleteUserSuccessTest() throws Exception {
		//given
		User user = getUser();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		//when
		Long result = userService.deleteUser(USER_ID);

		//then
		verify(userRepository, times(1)).delete(user);
		assertEquals(USER_ID, result);
	}

	@Test
	void updateGithubUrlTest() throws Exception {
		User user = getUser();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		String result = userService.updateGithubUrl(USER_ID, githubUrlRequest());

		assertEquals(result, user.getGithubUrl());
	}

	@Test
	void updateBlogUrlTest() throws Exception {
		User user = getUser();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		String result = userService.updateBlogUrl(USER_ID, blogUrlRequest());

		assertEquals(result, user.getBlogUrl());
	}

	@Test
	void updateBaekjoonIdTest() throws Exception {
		User user = getUser();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		String result = userService.updateBaekjoonId(USER_ID, baekjoonIdRequest());

		assertEquals(result, user.getBaekjoonId());
	}

	@Test
	void updateNicknameAndIntroduceSuccessTesT() throws Exception {
		User user = getUser();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		given(userRepository.existsUserByNickname(NEW_NICKNAME)).willReturn(false);

		userService.updateNicknameAndIntroduce(USER_ID, nicknameAndIntroduceRequest());

		assertEquals(nicknameAndIntroduceRequest().getNickname(), user.getNickname());
		assertEquals(nicknameAndIntroduceRequest().getIntroduce(), user.getIntroduce());
	}

	@Test
	void updateIntroduceSuccessTest1() throws Exception {
		User user = getUser();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		userService.updateNicknameAndIntroduce(USER_ID, introduceRequest());

		assertEquals(introduceRequest().getNickname(), user.getNickname());
		assertEquals(introduceRequest().getIntroduce(), user.getIntroduce());
	}

	@Test
	void updateNicknameAndIntroduceThrowExceptionTest() throws Exception {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(getUser()));
		given(userRepository.existsUserByNickname(NEW_NICKNAME)).willReturn(true);

		assertThrows(EmailDuplicateException.class,
			() -> userService.updateNicknameAndIntroduce(USER_ID, nicknameAndIntroduceRequest()));
	}

	@Test
	void getUserProfileTest() throws Exception {
		User user = getUserForProfile();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		UserProfileResponse result = userService.getUserProfile(USER_ID);

		assertEquals(objectMapper.writeValueAsString(result), objectMapper.writeValueAsString(userProfileResponse()));

	}

	@Test
	void updateUserProfileImageTest() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("image", "test.png",
			MediaType.IMAGE_PNG_VALUE, Base64.getEncoder().encode("image".getBytes()));
		String newImageUrl = "https://bucket.s3.ap-northeast-2.amazonaws.com/1/profile.png";
		User user = getUserForProfile();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		given(s3UploadUtil.uploadProfileImage(any(), eq(USER_ID))).willReturn(newImageUrl);

		String result = userService.updateUserProfileImage(USER_ID, multipartFile);

		assertEquals(user.getImageUrl(), newImageUrl);
		assertEquals(result, newImageUrl);
	}

	@Test
	void updateUserProfileImageThrowExceptionTest() {
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(getUserForProfile()));
		given(s3UploadUtil.uploadProfileImage(any(), eq(USER_ID))).willThrow(ImageUploadFailureException.class);

		assertThrows(ImageUploadFailureException.class,
			() -> userService.updateUserProfileImage(USER_ID, null), "업로드를 실패 하였습니다.");
	}
}