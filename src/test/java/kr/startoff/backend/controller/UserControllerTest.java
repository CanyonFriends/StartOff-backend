package kr.startoff.backend.controller;

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

import kr.startoff.backend.controller.annotation.WithUserPrincipal;
import kr.startoff.backend.global.config.SecurityConfig;
import kr.startoff.backend.domain.user.controller.UserController;
import kr.startoff.backend.domain.user.dto.request.UserPasswordChangeRequest;
import kr.startoff.backend.global.common.dto.CommonResponse;
import kr.startoff.backend.domain.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	}
)
class UserControllerTest {
	MockMvc mockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;
	@MockBean
	UserService userService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void validateDuplicateEmailAndNicknameThrowBadRequestExceptionTest1() throws Exception {
		mockMvc.perform(get("/api/v1/users/validation"))
			.andExpect(status().isBadRequest());
	}

	@Test
	void validateDuplicateEmailAndNicknameThrowBadRequestExceptionTest2() throws Exception {
		mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("nickname", NICKNAME)
				.queryParam("email", EMAIL))
			.andExpect(status().isBadRequest());
	}

	@Test
	void changeUserPasswordTest() throws Exception {
		CommonResponse response = new CommonResponse(true, "비밀번호가 변경되었습니다.");
		given(userService.changeUserPassword(any(UserPasswordChangeRequest.class), eq(USER_ID))).willReturn(true);
		String content = objectMapper.writeValueAsString(response);

		MvcResult result = mockMvc.perform(put("/api/v1/users/{user_id}/password", USER_ID)
				.content(objectMapper.writeValueAsString(passwordChangeRequest()))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.accept(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}

	@Test
	void leaveMembershipTest() throws Exception {
		given(userService.deleteUser(USER_ID)).willReturn(USER_ID);

		MvcResult result = mockMvc.perform(delete("/api/v1/users/{user_id}", USER_ID))
			.andExpect(status().isOk())
			.andReturn();

		assertEquals(result.getResponse().getContentAsString(), USER_ID.toString());
	}

	@Test
	@WithUserPrincipal
	void getSelfInformationTest() throws Exception {
		String content = objectMapper.writeValueAsString(userInfoResponse());

		MvcResult result = mockMvc.perform(get("/api/v1/users/self")).andReturn();

		assertEquals(content, result.getResponse().getContentAsString());
	}
}