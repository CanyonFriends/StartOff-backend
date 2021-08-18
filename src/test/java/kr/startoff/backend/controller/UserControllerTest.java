package kr.startoff.backend.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import kr.startoff.backend.config.SecurityConfig;
import kr.startoff.backend.exception.custom.InvalidPasswordException;
import kr.startoff.backend.payload.response.UserInfoResponse;
import kr.startoff.backend.service.UserService;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static kr.startoff.backend.prototype.UserPrototype.*;
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

	@Autowired
	MockMvc mockMvc;

	@MockBean
	UserService userService;

	@WithMockUser
	@Test
	void validateDuplicationEmailTest() throws Exception {
		given(userService.isDuplicateEmail(EMAIL)).willReturn(false);

		mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("email", EMAIL))
			.andExpect(status().isNoContent());
	}

	@WithMockUser
	@Test
	void validateDuplicationEmailWithThrowExceptionTest() throws Exception {
		given(userService.isDuplicateEmail(EMAIL)).willReturn(true);

		mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("email", EMAIL))
			.andExpect(status().isConflict());
	}

	@WithMockUser
	@Test
	void validateDuplicationNicknameTest() throws Exception {
		given(userService.isDuplicateNickname(NICKNAME)).willReturn(false);

		mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("nickname", NICKNAME))
			.andExpect(status().isNoContent());
	}

	@WithMockUser
	@Test
	void validateDuplicationNicknameWithThrowExceptionTest() throws Exception {
		given(userService.isDuplicateNickname(NICKNAME)).willReturn(true);

		mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("nickname", NICKNAME))
			.andExpect(status().isConflict());
	}
}