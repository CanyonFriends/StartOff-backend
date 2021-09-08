package kr.startoff.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.entity.Project;
import kr.startoff.backend.entity.SkillTag;
import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.custom.ProjectBadRequest;
import kr.startoff.backend.exception.custom.ProjectNotFoundException;
import kr.startoff.backend.exception.custom.UserNotFoundException;
import kr.startoff.backend.payload.request.ProjectRequest;
import kr.startoff.backend.payload.response.ProjectResponse;
import kr.startoff.backend.repository.ProjectRepository;
import kr.startoff.backend.repository.SkillTagRepository;
import kr.startoff.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;
	private final SkillTagRepository skillTagRepository;

	@Transactional
	public ProjectResponse saveProject(ProjectRequest projectRequest, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		List<SkillTag> projectSkills = extractProjectSkills(projectRequest);
		Project project = Project.createProject(user, projectRequest, projectSkills);
		return new ProjectResponse(projectRepository.save(project));
	}

	@Transactional
	public ProjectResponse updateProject(ProjectRequest projectRequest, Long userId, Long projectId) {
		Project project = projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);
		if (!project.getUser().getId().equals(userId)) {
			throw new ProjectBadRequest();
		}
		List<SkillTag> projectSkills = extractProjectSkills(projectRequest);
		project.updateProject(projectRequest, projectSkills);
		return new ProjectResponse(project);
	}

	@Transactional
	public Long deleteProject(Long userId, Long projectId) {
		Project project = projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		if (!project.getUser().getId().equals(userId)) {
			throw new ProjectBadRequest();
		}
		user.getProjects().remove(project);
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
