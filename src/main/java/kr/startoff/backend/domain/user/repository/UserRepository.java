package kr.startoff.backend.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.startoff.backend.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	boolean existsUserByEmail(String email);

	boolean existsUserByNickname(String nickname);
}
