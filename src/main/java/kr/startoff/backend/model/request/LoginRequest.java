package kr.startoff.backend.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class LoginRequest {
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String password;
}
