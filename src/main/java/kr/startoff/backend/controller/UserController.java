package kr.startoff.backend.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.startoff.backend.exception.custom.EmailOrNicknameDuplicateException;
import kr.startoff.backend.payload.request.UserInfoUpdateRequest;
import kr.startoff.backend.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
	private final UserService userService;

	@GetMapping("/users/validation")
	public ResponseEntity<?> validateDuplicationEmailOrNickname(
		@RequestParam Optional<String> email, @RequestParam Optional<String> nickname) {
		if (email.isPresent() && nickname.isEmpty()) {
			if (userService.isDuplicateEmail(email.get())) {
				throw new EmailOrNicknameDuplicateException("Email이 중복되었습니다.");
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else if (email.isEmpty() && nickname.isPresent()) {
			if (userService.isDuplicateNickname(nickname.get())) {
				throw new EmailOrNicknameDuplicateException("Nickname이 중복되었습니다.");
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// TODO : Design 이 결정 되면 확실하게 로직 설계
	@GetMapping("/users/{user_id}")
	public ResponseEntity<?> getUserInformation(@PathVariable(value = "user_id") Long userId) {
		return ResponseEntity.ok(userService.getUserInformation(userId));
	}

	// TODO : Design 이 결정 되면 확실하게 로직 설계
	@PutMapping("/users/{user_id}")
	public ResponseEntity<?> updateUserInformation(@PathVariable(value = "user_id") Long userId,
		@RequestBody UserInfoUpdateRequest updateRequest) {
		return ResponseEntity.ok(userService.updateUser(updateRequest, userId));
	}

	@DeleteMapping("/users/{user_id}")
	public ResponseEntity<?> leaveMembership(@PathVariable(value = "user_id") Long userId) {
		return ResponseEntity.ok(userService.deleteUser(userId));
	}

	@GetMapping("/users/profile/{user_id}")
	public ResponseEntity<?> getUserProfile(@PathVariable(value = "user_id") Long userId) {
		return ResponseEntity.ok(userService.getUserProfile(userId));
	}

}
