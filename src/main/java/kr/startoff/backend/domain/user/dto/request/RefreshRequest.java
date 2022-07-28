package kr.startoff.backend.domain.user.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshRequest {
	@NotBlank
	private String uuid;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String accessToken;

	public RefreshRequest(String uuid, String email, String accessToken) {
		this.uuid = uuid;
		this.email = email;
		this.accessToken = accessToken;
	}
}
