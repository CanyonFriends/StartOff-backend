package kr.startoff.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static kr.startoff.backend.prototype.UserPrototype.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.custom.InvalidPasswordException;
import kr.startoff.backend.exception.custom.UserNotFoundException;
import kr.startoff.backend.payload.response.UserInfoResponse;
import kr.startoff.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	private UserRepository userRepository;
	private PasswordEncoder encoder;
	private UserService userService;

	@BeforeEach
	private void setUp() {
		userRepository = mock(UserRepository.class);
		encoder = new BCryptPasswordEncoder(4);
		userService = new UserService(userRepository, encoder);
	}

	@Test
	void signUpTest() throws Exception {
		//given
		given(userRepository.save(any())).willReturn(user(encoder));
		//when
		User createdUser = userService.signUp(signupRequest());
		//then
		assertNotNull(createdUser);
		assertEquals(createdUser.getEmail(), signupRequest().getEmail());
		assertEquals(createdUser.getNickname(), signupRequest().getNickname());
		assertTrue(encoder.matches(signupRequest().getPassword(), createdUser.getPassword()));
	}

	@Test
	void changeUserPasswordTest() throws Exception {
		//given
		User user = user(encoder);
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
		//given
		User user = user(encoder);
		String InvalidPassword = "InvalidPassword";
		user.setPassword(encoder.encode(InvalidPassword));
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		//when

		//then
		assertThrows(InvalidPasswordException.class,
			() -> userService.changeUserPassword(userPasswordChangeRequest(), USER_ID),
			"비밀번호를 확인해주세요.");
	}

	@Test
	void getUserInformationTest() throws Exception {
		//given
		User user = user(encoder);
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
}