package kr.startoff.backend.domain.user.dto.response;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.domain.security.UserPrincipal;
import lombok.Getter;

@Getter
public class UserInfoResponse {
	@NotBlank
	private Long userId;

	@NotBlank
	private String email;

	@NotBlank
	private String nickname;

	private UserInfoResponse(Long userId, String email, String nickname) {
		this.userId = userId;
		this.email = email;
		this.nickname = nickname;
	}

	public static UserInfoResponse from(User user) {
		return new UserInfoResponse(user.getId(), user.getEmail(), user.getNickname());
	}

	public static UserInfoResponse from(UserPrincipal user) {
		return new UserInfoResponse(user.getId(), user.getEmail(), user.getNickname());
	}
}