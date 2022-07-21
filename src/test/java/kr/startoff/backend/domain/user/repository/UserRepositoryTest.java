package kr.startoff.backend.domain.user.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.domain.user.domain.AuthProvider;
import kr.startoff.backend.domain.user.domain.User;

@DataJpaTest
class UserRepositoryTest {
	private final String EMAIL = "proto_seo@naver.com";
	private final String NICKNAME = "proto_seo";
	private final String PASSWORD = "password";
	private final User USER = User.builder()
		.email(EMAIL)
		.nickname(NICKNAME)
		.password(PASSWORD)
		.provider(AuthProvider.LOCAL)
		.build();

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	private void saveUser() {
		userRepository.save(USER);
	}

	@AfterEach
	private void deleteUser() {
		userRepository.delete(USER);
	}

	@Test
	void findByEmailTest() throws Exception {
		//given
		//when
		Optional<User> foundUser = userRepository.findByEmail(EMAIL);
		//then
		assertNotNull(foundUser);
		assertTrue(foundUser.isPresent());
		assertEquals(EMAIL, foundUser.get().getEmail());
		assertEquals(PASSWORD, foundUser.get().getPassword());
		assertEquals(NICKNAME, foundUser.get().getNickname());
		assertEquals(AuthProvider.LOCAL, foundUser.get().getProvider());
	}

	@Test
	void existsUserByEmailTest() throws Exception {
		//given
		//when
		boolean result = userRepository.existsUserByEmail(EMAIL);
		//then
		assertTrue(result);
	}

	@Test
	void existsUserByNicknameTest() throws Exception {
		//given
		//when
		boolean result = userRepository.existsUserByNickname(NICKNAME);
		//then
		assertTrue(result);
	}
}