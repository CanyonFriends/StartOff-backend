package kr.startoff.backend.controller;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.common.config.SecurityConfig;
import kr.startoff.backend.domain.tag.controller.SkillTagController;
import kr.startoff.backend.domain.tag.dto.SkillTagResponse;
import kr.startoff.backend.domain.tag.service.SkillTagService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SkillTagController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	}
)
class SkillTagControllerTest {
	@MockBean
	private SkillTagService skillTagService;
	private final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	MockMvc mockMvc;


	@Test
	@WithMockUser
	void getAllSkillTagTest() throws Exception {
		List<SkillTagResponse> allSkillTag = getSkillTagList().stream()
			.map(SkillTagResponse::new)
			.collect(Collectors.toList());
		given(skillTagService.getAllSkillTag()).willReturn(allSkillTag);

		String content = objectMapper.writeValueAsString(allSkillTag);
		ResultActions result = mockMvc.perform(get("/api/v1/skills"));

		result.andExpect(status().isOk())
			.andExpect(content().string(content));
	}
}