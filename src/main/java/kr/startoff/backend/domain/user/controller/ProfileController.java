package kr.startoff.backend.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.startoff.backend.domain.user.dto.request.ProfileRequest;
import kr.startoff.backend.domain.user.service.ProfileService;
import kr.startoff.backend.domain.user.dto.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProfileController {
	private final ProfileService profileService;

	@GetMapping("/users/{userId}/profile")
	public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long userId) {
		return ResponseEntity.ok(profileService.getUserProfile(userId));
	}

	@PutMapping("/users/{userId}/profile")
	public ResponseEntity<UserProfileResponse> updateUserProfile(@PathVariable Long userId,
		@RequestBody ProfileRequest profileRequest) {
		UserProfileResponse response = profileService.updateProfile(userId, profileRequest);
		return ResponseEntity.ok(response);
	}
}
