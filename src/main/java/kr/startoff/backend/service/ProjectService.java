package kr.startoff.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.entity.Project;
import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.custom.ProjectBadRequest;
import kr.startoff.backend.exception.custom.ProjectNotFoundException;
import kr.startoff.backend.exception.custom.UserNotFoundException;
import kr.startoff.backend.payload.request.ProjectRequest;
import kr.startoff.backend.payload.response.ProjectResponse;
import kr.startoff.backend.repository.ProjectRepository;
import kr.startoff.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	@Transactional
	public ProjectResponse saveProject(ProjectRequest projectRequest, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		Project project = Project.createProject(user, projectRequest);
		return new ProjectResponse(projectRepository.save(project));
	}

	@Transactional
	public ProjectResponse updateProject(ProjectRequest projectRequest, Long userId, Long projectId) {
		Project project = projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);
		if (!project.getUser().getId().equals(userId)) {
			throw new ProjectBadRequest();
		}
		project.updateProject(projectRequest);
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

}
