package kr.startoff.backend.domain.user.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String nickname;
	@NotBlank
	private String password;

	public SignupRequest(String email, String nickname, String password) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
	}
}
