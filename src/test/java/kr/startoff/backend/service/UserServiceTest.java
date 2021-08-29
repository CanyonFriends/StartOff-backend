package kr.startoff.backend.service;

import static kr.startoff.backend.payload.PayloadFixture.*;
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

import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.custom.EmailOrNicknameDuplicateException;
import kr.startoff.backend.exception.custom.InvalidPasswordException;
import kr.startoff.backend.exception.custom.UserNotFoundException;
import kr.startoff.backend.payload.response.UserInfoResponse;
import kr.startoff.backend.payload.response.UserProfileResponse;
import kr.startoff.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	private UserRepository userRepository;
	private PasswordEncoder encoder;
	private UserService userService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	private void setUp() {
		userRepository = mock(UserRepository.class);
		encoder = new BCryptPasswordEncoder(4);
		userService = new UserService(userRepository, encoder);
	}

	@Test
	void signUpTest() throws Exception {
		//given
		User user = getUser(encoder);
		given(userRepository.save(any())).willReturn(user);
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
	void isDuplicateEmailTest() throws Exception {
		when(userRepository.existsUserByEmail(EMAIL)).thenReturn(true);
		when(userRepository.existsUserByEmail(NEW_EMAIL)).thenReturn(false);

		assertTrue(userService.isDuplicateEmail(EMAIL));
		assertFalse(userService.isDuplicateEmail(NEW_EMAIL));
	}

	@Test
	void isDuplicateNicknameTest() throws Exception {
		when(userRepository.existsUserByNickname(NICKNAME)).thenReturn(true);
		when(userRepository.existsUserByNickname(NEW_NICKNAME)).thenReturn(false);

		assertTrue(userService.isDuplicateNickname(NICKNAME));
		assertFalse(userService.isDuplicateNickname(NEW_NICKNAME));
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

		assertThrows(EmailOrNicknameDuplicateException.class,
			() -> userService.updateNicknameAndIntroduce(USER_ID, nicknameAndIntroduceRequest()));
	}

	@Test
	void getUserProfileTest() throws Exception {
		User user = getUserForProfile();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));

		UserProfileResponse result = userService.getUserProfile(USER_ID);

		assertEquals(objectMapper.writeValueAsString(result), objectMapper.writeValueAsString(userProfileResponse()));

	}
}