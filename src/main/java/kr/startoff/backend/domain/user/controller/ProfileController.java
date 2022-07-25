package kr.startoff.backend.domain.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.startoff.backend.domain.user.dto.request.ProfileRequest;
import kr.startoff.backend.domain.user.dto.request.SkillTagRequest;
import kr.startoff.backend.domain.user.service.ProfileService;
import kr.startoff.backend.global.common.dto.CommonResponse;
import kr.startoff.backend.domain.tag.dto.SkillTagResponse;
import kr.startoff.backend.domain.user.dto.response.UserProfileResponse;
import kr.startoff.backend.domain.tag.service.SkillTagService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProfileController {
	private final ProfileService profileService;
	private final SkillTagService skillTagService;

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

	// TODO: SkillTag 관련 리팩토링 시 수정
	@PutMapping("/users/{userId}/skills")
	public ResponseEntity<SkillTagResponse> updateUserSkills(@PathVariable Long userId,
		@RequestBody SkillTagRequest skillTagRequest) {
		SkillTagResponse skillTagResponse = skillTagService.addSkillTagToUser(userId, skillTagRequest.getSkillName());
		return ResponseEntity.ok(skillTagResponse);
	}

	@DeleteMapping("/users/{userId}/skills/{skillId}")
	public ResponseEntity<CommonResponse> deleteUserSkill(@PathVariable Long userId, @PathVariable Long skillId) {
		skillTagService.deleteSkillTagToUser(userId, skillId);
		return ResponseEntity.ok(new CommonResponse(true, "해당 기술태그가 삭제되었습니다."));
	}
}
