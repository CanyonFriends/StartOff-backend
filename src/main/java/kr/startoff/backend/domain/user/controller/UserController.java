package kr.startoff.backend.domain.user.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.startoff.backend.domain.user.dto.request.NicknameRequest;
import kr.startoff.backend.domain.user.dto.request.UserPasswordChangeRequest;
import kr.startoff.backend.global.common.dto.CommonResponse;
import kr.startoff.backend.domain.user.dto.response.UserInfoResponse;
import kr.startoff.backend.domain.user.domain.security.UserPrincipal;
import kr.startoff.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
	private final UserService userService;

	@GetMapping("/users/validation")
	public ResponseEntity<Void> validateDuplicationEmailOrNickname(
		@RequestParam(defaultValue = "", required = false) String email,
		@RequestParam(defaultValue = "", required = false) String nickname) {
		if (userService.validateEmailOrNickname(email, nickname)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/users/{userId}/password")
	public ResponseEntity<CommonResponse> changeUserPassword(@PathVariable Long userId,
		@Valid @RequestBody UserPasswordChangeRequest updateRequest) {
		CommonResponse result = new CommonResponse(userService.changeUserPassword(updateRequest, userId),
			"비밀번호가 변경되었습니다.");
		return ResponseEntity.ok(result);
	}

	@PostMapping("/users/{userId}/image")
	public ResponseEntity<String> updateProfileImage(@PathVariable Long userId,
		@RequestPart("image") MultipartFile multipartFile) {
		return ResponseEntity.ok(userService.updateUserProfileImage(userId, multipartFile));
	}

	@PutMapping("/users/{userId}/nickname")
	public ResponseEntity<String> updateUserNickname(@PathVariable Long userId,
		@RequestBody NicknameRequest nicknameRequest) {
		return ResponseEntity.ok(userService.updateNickname(userId, nicknameRequest));
	}

	@DeleteMapping("/users/{userId}")
	public ResponseEntity<Long> leaveMembership(@PathVariable Long userId) {
		return ResponseEntity.ok(userService.deleteUser(userId));
	}

	@GetMapping("/users/self")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<UserInfoResponse> getSelfInformation(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		UserInfoResponse response = UserInfoResponse.from(userPrincipal);
		return ResponseEntity.ok(response);
	}
}
