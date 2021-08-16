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
	public void validateDuplicationEmailTest() throws Exception {
		given(userService.isDuplicateEmail(EMAIL)).willReturn(false);

		mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("email", EMAIL))
			.andExpect(status().isNoContent());
	}

	@WithMockUser
	@Test
	public void validateDuplicationEmailWithThrowExceptionTest() throws Exception {
		given(userService.isDuplicateEmail(EMAIL)).willReturn(true);

		mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("email", EMAIL))
			.andExpect(status().isConflict());
	}

	@WithMockUser
	@Test
	public void validateDuplicationNicknameTest() throws Exception {
		given(userService.isDuplicateNickname(NICKNAME)).willReturn(false);

		mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("nickname", NICKNAME))
			.andExpect(status().isNoContent());
	}

	@WithMockUser
	@Test
	public void validateDuplicationNicknameWithThrowExceptionTest() throws Exception {
		given(userService.isDuplicateNickname(NICKNAME)).willReturn(true);

		mockMvc.perform(get("/api/v1/users/validation")
				.queryParam("nickname", NICKNAME))
			.andExpect(status().isConflict());
	}

	@WithMockUser
	@Test
	public void getUserInformationTest() throws Exception {
		//given
		given(userService.getUserInformation(USER_ID)).willReturn(userInfo());
		ObjectMapper objectMapper = new ObjectMapper();
		//when
		mockMvc.perform(get("/api/v1/users/" + USER_ID))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(userInfo())));

		//then
	}

	@WithMockUser
	@Test
	public void changeUserPasswordSuccessTest() throws Exception {
		//given
		given(userService.changeUserPassword(userPasswordChangeRequest(), USER_ID))
			.willReturn(userInfo());

		ObjectMapper objectMapper = new ObjectMapper();
		//when
		// mockMvc.perform(put("/api/v1/users/"+USER_ID)
		// 		.contentType(MediaType.APPLICATION_JSON)
		// 		.content(objectMapper.writeValueAsString(userPasswordChangeRequest())))
		// 	.andExpect(status().isOk())
		// 	.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		//then
	}

	@WithMockUser
	@Test
	public void changeUserPasswordFailTest() throws Exception {
		//given
		given(userService.changeUserPassword(userPasswordChangeRequest(), USER_ID))
			.willThrow(InvalidPasswordException.class);

		ObjectMapper objectMapper = new ObjectMapper();
		//when

		//then
	}
	/*
	    @Test
    void saveUsers() throws Exception {
        when(usersService.saveUser(any())).thenReturn(aUserDTO());
        mockMvc.perform(post("/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aUserDTO())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(aUserDTO())));
    }

	 */
}