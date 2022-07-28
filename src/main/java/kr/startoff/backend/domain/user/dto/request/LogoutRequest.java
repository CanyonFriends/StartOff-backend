package kr.startoff.backend.domain.user.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogoutRequest {
	@NotBlank
	private String uuid;

	@NotBlank
	private String accessToken;

	public LogoutRequest(String uuid, String accessToken) {
		this.uuid = uuid;
		this.accessToken = accessToken;
	}
}
