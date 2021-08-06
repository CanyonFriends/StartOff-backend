package kr.startoff.backend.model.response;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponse {
	@NotBlank
	private String email;

	@NotBlank
	private String nickname;

	@Builder
	public UserInfoResponse(String email, String nickname) {
		this.email = email;
		this.nickname = nickname;
	}
}