package kr.startoff.backend.domain.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccessTokenResponse {
	private Long userId;
	private String accessToken;

	private AccessTokenResponse(Long userId, String accessToken) {
		this.userId = userId;
		this.accessToken = accessToken;
	}

	public static AccessTokenResponse of(Long userId, String accessToken) {
		return new AccessTokenResponse(userId, accessToken);
	}
}
