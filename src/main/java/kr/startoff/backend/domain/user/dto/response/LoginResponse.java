package kr.startoff.backend.domain.user.dto.response;

import kr.startoff.backend.domain.user.domain.security.UserPrincipal;
import lombok.Getter;

@Getter
public class LoginResponse {
	private final Long userId;
	private final String email;
	private final String nickname;
	private final String accessToken;
	private final String uuid;

	private LoginResponse(Long userId, String email, String nickname, String accessToken, String uuid) {
		this.userId = userId;
		this.email = email;
		this.nickname = nickname;
		this.accessToken = accessToken;
		this.uuid = uuid;
	}

	public static LoginResponse of(UserPrincipal userPrincipal, String accessToken, String uuid) {
		return new LoginResponse(
			userPrincipal.getId(),
			userPrincipal.getEmail(),
			userPrincipal.getNickname(),
			accessToken,
			uuid);
	}
}
