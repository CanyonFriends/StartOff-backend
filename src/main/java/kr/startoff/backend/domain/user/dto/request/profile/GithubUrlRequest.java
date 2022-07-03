package kr.startoff.backend.domain.user.dto.request.profile;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubUrlRequest {
	private String githubUrl;

	public GithubUrlRequest(String githubUrl) {
		this.githubUrl = githubUrl;
	}
}
