package kr.startoff.backend.payload.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RefreshResponse {
	private Long userId;
	private String email;
	private String nickname;

	public RefreshResponse(Long userId, String email, String nickname) {
		this.userId = userId;
		this.email = email;
		this.nickname = nickname;
	}
}
