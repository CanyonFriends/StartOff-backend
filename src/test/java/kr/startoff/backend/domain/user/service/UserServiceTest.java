package kr.startoff.backend.domain.user.service;

import static kr.startoff.backend.domain.user.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.user.dto.response.UserInfoResponse;
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
		User user = user();
		String beforePassword = user.getPassword();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		//when
		userService.changeUserPassword(userPasswordChangeRequest(), USER_ID);
		Optional<User> changePasswordUser = userRepository.findById(USER_ID);

		//then
		assertNotNull(changePasswordUser);
		assertTrue(changePasswordUser.isPresent());
		assertNotEquals(changePasswordUser.get().getPassword(), beforePassword);
	}

	@Test
	void changeUserPasswordThrowInvalidPasswordExceptionTest() throws Exception {
		User user = user();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		assertThrows(UserException.class,
			() -> userService.changeUserPassword(userPasswordChangeRequest(), USER_ID),
			"비밀번호를 확인해주세요.");
	}

	@Test
	void getUserInformationTest() throws Exception {
		//given
		User user = user();
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
		when(userRepository.findById(USER_ID)).thenThrow(UserException.class);

		assertThrows(UserException.class,
			() -> userService.deleteUser(USER_ID),
			"해당 유저를 찾을 수 없습니다.");
	}

	@Test
	void deleteUserSuccessTest() throws Exception {
		//given
		User user = user();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		//when
		Long result = userService.deleteUser(USER_ID);

		//then
		verify(userRepository, times(1)).delete(user);
		assertEquals(USER_ID, result);
	}
}