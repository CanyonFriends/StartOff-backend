package kr.startoff.backend.domain.project.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.startoff.backend.domain.project.dto.ProjectRequest;
import kr.startoff.backend.domain.project.dto.ProjectResponse;
import kr.startoff.backend.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProjectController {
	private final ProjectService projectService;

	@PostMapping("/users/{user_id}/projects")
	public ResponseEntity<ProjectResponse> saveProject(@PathVariable(value = "user_id") Long userId,
		@RequestBody ProjectRequest projectRequest) {
		ProjectResponse project = projectService.saveProject(projectRequest, userId);

		Map<String, Long> uriComponents = new HashMap<>();
		uriComponents.put("user_id", userId);
		uriComponents.put("project_id", project.getId());

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/users/{user_id}/projects/{project_id}")
			.buildAndExpand(uriComponents)
			.toUri();

		return ResponseEntity.created(location).body(project);
	}

	@PutMapping("/users/{user_id}/projects/{project_id}")
	public ResponseEntity<ProjectResponse> updateProject(@PathVariable(value = "user_id") Long userId,
		@PathVariable(value = "project_id") Long projectId, @RequestBody ProjectRequest projectRequest) {
		ProjectResponse project = projectService.updateProject(projectRequest, userId, projectId);
		return new ResponseEntity<>(project, HttpStatus.OK);
	}

	@DeleteMapping("/users/{user_id}/projects/{project_id}")
	public ResponseEntity<Long> deleteProject(@PathVariable(value = "user_id") Long userId,
		@PathVariable(value = "project_id") Long projectId) {
		return ResponseEntity.ok(projectService.deleteProject(userId,projectId));

	}
}

