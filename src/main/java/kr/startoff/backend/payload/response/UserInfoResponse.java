package kr.startoff.backend.payload.response;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoResponse {
	@NotBlank
	private Long userId;

	@NotBlank
	private String email;

	@NotBlank
	private String nickname;

	public UserInfoResponse(Long userId, String email, String nickname) {
		this.userId = userId;
		this.email = email;
		this.nickname = nickname;
	}
}