package kr.startoff.backend.domain.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.startoff.backend.domain.user.dto.request.profile.BaekjoonIdRequest;
import kr.startoff.backend.domain.user.dto.request.profile.BlogUrlRequest;
import kr.startoff.backend.domain.user.dto.request.profile.GithubUrlRequest;
import kr.startoff.backend.domain.user.dto.request.profile.NicknameAndIntroduceRequest;
import kr.startoff.backend.domain.user.dto.request.profile.SkillTagRequest;
import kr.startoff.backend.global.common.dto.CommonResponse;
import kr.startoff.backend.domain.tag.dto.SkillTagResponse;
import kr.startoff.backend.domain.user.dto.response.UserProfileResponse;
import kr.startoff.backend.domain.tag.service.SkillTagService;
import kr.startoff.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProfileController {
	private final UserService userService;
	private final SkillTagService skillTagService;

	@GetMapping("/users/{user_id}/profile")
	public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable(value = "user_id") Long userId) {
		return ResponseEntity.ok(userService.getUserProfile(userId));
	}

	@PutMapping("/users/{user_id}/introduce")
	public ResponseEntity<Map<String, String>> updateUserNicknameAndIntroduce(
		@PathVariable(value = "user_id") Long userId,
		@RequestBody NicknameAndIntroduceRequest nicknameAndIntroduceRequest) {
		String value = userService.updateNicknameAndIntroduce(userId, nicknameAndIntroduceRequest);
		Map<String, String> result = makeResponseBody("nickname", value);
		result.put("introduce", nicknameAndIntroduceRequest.getIntroduce());
		return ResponseEntity.ok(result);
	}

	@PutMapping("/users/{user_id}/github-url")
	public ResponseEntity<Map<String, String>> updateUserGithubUrl(@PathVariable(value = "user_id") Long userId,
		@RequestBody GithubUrlRequest githubUrlRequest) {
		String value = userService.updateGithubUrl(userId, githubUrlRequest);
		Map<String, String> result = makeResponseBody("github_url", value);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/users/{user_id}/blog-url")
	public ResponseEntity<Map<String, String>> updateUserBlogUrl(@PathVariable(value = "user_id") Long userId,
		@RequestBody BlogUrlRequest blogUrlRequest) {
		String value = userService.updateBlogUrl(userId, blogUrlRequest);
		Map<String, String> result = makeResponseBody("blog_url", value);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/users/{user_id}/baekjoon-id")
	public ResponseEntity<Map<String, String>> updateUserBaekjoonId(@PathVariable(value = "user_id") Long userId,
		@RequestBody BaekjoonIdRequest baekjoonIdRequest) {
		String value = userService.updateBaekjoonId(userId, baekjoonIdRequest);
		Map<String, String> result = makeResponseBody("baekjoon_id", value);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/users/{user_id}/skills")
	public ResponseEntity<SkillTagResponse> updateUserSkills(@PathVariable(value = "user_id") Long userId,
		@RequestBody SkillTagRequest skillTagRequest) {
		SkillTagResponse skillTagResponse = skillTagService.addSkillTagToUser(userId, skillTagRequest.getSkillName());
		return ResponseEntity.ok(skillTagResponse);
	}

	@PostMapping("/users/{user_id}/image")
	public ResponseEntity<String> updateUserProfileImage(@PathVariable(value = "user_id") Long userId,
		@RequestPart("image") MultipartFile multipartFile) {
		return ResponseEntity.ok(userService.updateUserProfileImage(userId, multipartFile));
	}

	@DeleteMapping("/users/{user_id}/skills/{skill_id}")
	public ResponseEntity<CommonResponse> deleteUserSkill(@PathVariable(value = "user_id") Long userId,
		@PathVariable(value = "skill_id") Long skillId) {
		skillTagService.deleteSkillTagToUser(userId, skillId);
		return ResponseEntity.ok(new CommonResponse(true, "해당 기술태그가 삭제되었습니다."));
	}

	private Map<String, String> makeResponseBody(String key, String value) {
		Map<String, String> result = new HashMap<>();
		result.put(key, value);
		return result;
	}
}
