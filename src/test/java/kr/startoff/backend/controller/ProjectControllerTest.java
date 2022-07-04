package kr.startoff.backend.controller;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.global.config.SecurityConfig;
import kr.startoff.backend.domain.project.controller.ProjectController;
import kr.startoff.backend.domain.project.dto.ProjectRequest;
import kr.startoff.backend.domain.project.service.ProjectService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProjectController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	}
)
class ProjectControllerTest {
	@MockBean
	private ProjectService projectService;

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	private void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void saveProjectTest() throws Exception {
		given(projectService.saveProject(any(ProjectRequest.class), eq(USER_ID)))
			.willReturn(projectResponse());
		String content = objectMapper.writeValueAsString(projectResponse());

		MvcResult result = mockMvc.perform(post("/api/v1/users/{user_id}/projects", USER_ID)
				.content(objectMapper.writeValueAsString(projectRequest()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void updateProjectTest() throws Exception {
		given(projectService.updateProject(any(ProjectRequest.class), eq(USER_ID), eq(PROJECT_ID)))
			.willReturn(updateProjectResponse());
		String content = objectMapper.writeValueAsString(updateProjectResponse());

		MvcResult result = mockMvc.perform(put("/api/v1/users/{user_id}/projects/{project_id}", USER_ID, PROJECT_ID)
				.content(objectMapper.writeValueAsString(updateProjectRequest()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void deleteProjectTest() throws Exception {
		given(projectService.deleteProject(USER_ID,PROJECT_ID)).willReturn(PROJECT_ID);

		MvcResult result = mockMvc.perform(delete("/api/v1/users/{user_id}/projects/{project_id}", USER_ID, PROJECT_ID)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		assertTrue(result.getResponse().getContentAsString().contains(PROJECT_ID.toString()));
	}
}