package kr.startoff.backend.payload.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPasswordChangeRequest {
	@NotBlank
	private String beforePassword;
	@NotBlank
	private String afterPassword;

	public UserPasswordChangeRequest(String beforePassword, String afterPassword) {
		this.beforePassword = beforePassword;
		this.afterPassword = afterPassword;
	}
}
