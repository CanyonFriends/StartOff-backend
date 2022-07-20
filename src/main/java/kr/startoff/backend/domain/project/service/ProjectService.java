package kr.startoff.backend.domain.project.service;

import static kr.startoff.backend.global.exception.ExceptionType.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.domain.project.domain.Project;
import kr.startoff.backend.domain.project.exception.ProjectException;
import kr.startoff.backend.domain.tag.domain.SkillTag;
import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.project.dto.ProjectRequest;
import kr.startoff.backend.domain.project.dto.ProjectResponse;
import kr.startoff.backend.domain.project.repository.ProjectRepository;
import kr.startoff.backend.domain.tag.repository.SkillTagRepository;
import kr.startoff.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;
	private final SkillTagRepository skillTagRepository;

	@Transactional
	public ProjectResponse saveProject(ProjectRequest projectRequest, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
		List<SkillTag> projectSkills = extractProjectSkills(projectRequest);
		Project project = Project.createProject(user, projectRequest, projectSkills);
		return new ProjectResponse(projectRepository.save(project));
	}

	@Transactional
	public ProjectResponse updateProject(ProjectRequest projectRequest, Long userId, Long projectId) {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectException(PROJECT_NOT_FOUND));
		if (!project.getUser().getId().equals(userId)) {
			throw new ProjectException(PROJECT_FORBIDDEN);
		}
		List<SkillTag> projectSkills = extractProjectSkills(projectRequest);
		project.updateProject(projectRequest, projectSkills);
		return new ProjectResponse(project);
	}

	@Transactional
	public Long deleteProject(Long userId, Long projectId) {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectException(PROJECT_NOT_FOUND));
		User user = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
		if (!project.getUser().getId().equals(userId)) {
			throw new ProjectException(PROJECT_FORBIDDEN);
		}
		projectRepository.delete(project);
		return projectId;
	}

	private List<SkillTag> extractProjectSkills(ProjectRequest projectRequest) {
		return projectRequest.getProjectSkills()
			.stream()
			.map(skillTagRepository::findBySkillName)
			.distinct()
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
	}
}
