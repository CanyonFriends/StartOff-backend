package kr.startoff.backend.domain.user.dto.response;

import lombok.Getter;

@Getter
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
