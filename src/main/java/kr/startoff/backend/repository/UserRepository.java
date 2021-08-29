package kr.startoff.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.startoff.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	boolean existsUserByEmail(String email);

	boolean existsUserByNickname(String nickname);
}
