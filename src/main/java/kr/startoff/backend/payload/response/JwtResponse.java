package kr.startoff.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JwtResponse {
	@JsonProperty("token_type")
	private final String TOKEN_TYPE = "Bearer";
	private String accessToken;
	private String uuid;
	private Long userId;
	private String email;
	private String nickname;

	public JwtResponse(String accessToken, String uuid, Long userId, String email, String nickname) {
		this.accessToken = accessToken;
		this.uuid = uuid;
		this.userId = userId;
		this.email = email;
		this.nickname = nickname;
	}
}
