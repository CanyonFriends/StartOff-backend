package kr.startoff.backend.payload.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccessTokenResponse {
	private Long userId;
	private String accessToken;

	public AccessTokenResponse(Long userId, String accessToken) {
		this.userId = userId;
		this.accessToken = accessToken;
	}
}
