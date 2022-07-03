package kr.startoff.backend.domain.tag.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.startoff.backend.domain.tag.dto.SkillTagResponse;
import kr.startoff.backend.domain.tag.service.SkillTagService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SkillTagController {
	private final SkillTagService skillTagService;

	@GetMapping("/skills")
	public ResponseEntity<List<SkillTagResponse>> getAllSkillTag() {
		return ResponseEntity.ok(skillTagService.getAllSkillTag());
	}
}
