package kr.startoff.backend.domain.user.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RefreshOrLogoutRequest {
	@NotBlank
	private String uuid;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String accessToken;

	public RefreshOrLogoutRequest(String uuid, String email, String accessToken) {
		this.uuid = uuid;
		this.email = email;
		this.accessToken = accessToken;
	}
}
