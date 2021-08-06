package kr.startoff.backend.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UserInfoUpdateRequest {
	@NotBlank
	private String nickname;

	@NotBlank
	private String password;
}
