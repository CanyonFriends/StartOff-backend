package kr.startoff.backend.payload.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UserPasswordChangeRequest {
	@NotBlank
	private String beforePassword;
	@NotBlank
	private String afterPassword;
}
