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
