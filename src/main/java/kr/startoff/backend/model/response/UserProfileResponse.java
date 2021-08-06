package kr.startoff.backend.model.response;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserProfileResponse {
	@NotBlank
	private String nickname;

	private String introduce;

	private String githubUrl;

	private String blogUrl;

	private String baekjoonId;

	// TODO : Projects, TechTags 추가 예정

	@Builder
	public UserProfileResponse(String nickname, String introduce, String githubUrl, String blogUrl,
		String baekjoonId) {
		this.nickname = nickname;
		this.introduce = introduce;
		this.githubUrl = githubUrl;
		this.blogUrl = blogUrl;
		this.baekjoonId = baekjoonId;
	}


}
