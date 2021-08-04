package kr.startoff.backend.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class SignupRequest {
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String nickname;
	@NotBlank
	private String password;
}
