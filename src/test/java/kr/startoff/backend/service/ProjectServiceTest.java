package kr.startoff.backend.service;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.project.domain.Project;
import kr.startoff.backend.domain.project.service.ProjectService;
import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.project.exception.ProjectForbiddenException;
import kr.startoff.backend.domain.project.exception.ProjectNotFoundException;
import kr.startoff.backend.domain.project.dto.ProjectRequest;
import kr.startoff.backend.domain.project.dto.ProjectResponse;
import kr.startoff.backend.domain.project.repository.ProjectRepository;
import kr.startoff.backend.domain.tag.repository.SkillTagRepository;
import kr.startoff.backend.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
	private ProjectRepository projectRepository;
	private UserRepository userRepository;
	private SkillTagRepository skillTagRepository;
	private ProjectService projectService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	private void setUp() {
		projectRepository = mock(ProjectRepository.class);
		userRepository = mock(UserRepository.class);
		skillTagRepository = mock(SkillTagRepository.class);
		projectService = new ProjectService(projectRepository, userRepository, skillTagRepository);
	}

	@Test
	void saveProjectTest() throws Exception {
		User user = getUser();
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		given(projectRepository.save(any())).willReturn(getProject());

		ProjectResponse result = projectService.saveProject(projectRequest(), USER_ID);

		assertEquals(objectMapper.writeValueAsString(result), objectMapper.writeValueAsString(projectResponse()));
	}

	@Test
	void projectNotFoundExceptionTest() throws Exception {
		given(projectRepository.findById(PROJECT_ID)).willThrow(ProjectNotFoundException.class);

		final ProjectRequest request = projectRequest();

		assertThrows(ProjectNotFoundException.class,
			() -> projectService.updateProject(request, USER_ID, PROJECT_ID), "?????? ??????????????? ?????? ??? ????????????.");
	}

	@Test
	void updateProjectTest() throws Exception {
		Project project = getProject(getUser());
		given(projectRepository.findById(PROJECT_ID)).willReturn(Optional.of(project));

		ProjectResponse result = projectService.updateProject(updateProjectRequest(), USER_ID, PROJECT_ID);

		assertEquals(result.getTitle(), project.getTitle());
		assertEquals(result.getIntroduce(), project.getIntroduce());
		assertEquals(result.getContent(), project.getContent());
	}

	@Test
	void updateProjectThrowExceptionTest() throws Exception {
		Project project = getProject(getUser());
		given(projectRepository.findById(PROJECT_ID)).willReturn(Optional.of(project));
		Long wrongUserId = 123L;
		final ProjectRequest request = updateProjectRequest();

		assertThrows(ProjectForbiddenException.class,
			() -> projectService.updateProject(request, wrongUserId, PROJECT_ID), "????????? ???????????????.");
	}

	@Test
	void deleteProjectTest() throws Exception {
		User user = getUser();
		Project project = getProject(user);
		given(projectRepository.save(any())).willReturn(project);
		given(projectRepository.findById(PROJECT_ID)).willReturn(Optional.of(project));
		given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
		projectService.saveProject(projectRequest(), USER_ID);

		projectService.deleteProject(USER_ID,PROJECT_ID);
		assertFalse(user.getProjects().contains(project));
	}

	@Test
	void deleteProjectThrowExceptionTest() throws Exception {
		Project project = getProject(getUser());
		given(projectRepository.findById(PROJECT_ID)).willReturn(Optional.of(project));
		given(userRepository.findById(any())).willReturn(Optional.of(new User()));
		Long wrongUserId = 123L;

		assertThrows(ProjectForbiddenException.class,
			() -> projectService.deleteProject(wrongUserId, PROJECT_ID), "????????? ???????????????.");
	}
}