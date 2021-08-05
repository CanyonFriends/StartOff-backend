package kr.startoff.backend.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else if (email.isEmpty() && nickname.isPresent()) {
			if (userService.isDuplicateNickname(nickname.get())) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
