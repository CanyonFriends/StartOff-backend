package kr.startoff.backend.domain.user.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String password;

	public LoginRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
