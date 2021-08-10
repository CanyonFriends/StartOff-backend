package kr.startoff.backend.model.response;

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
	private String refreshToken;
	private Long userId;
	private String email;
	public JwtResponse(String accessToken, String refreshToken, Long userId, String email) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.userId = userId;
		this.email = email;
	}
}
