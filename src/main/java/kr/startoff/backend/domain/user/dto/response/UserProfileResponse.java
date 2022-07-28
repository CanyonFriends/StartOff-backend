package kr.startoff.backend.domain.user.dto.response;

import javax.validation.constraints.NotBlank;

import kr.startoff.backend.domain.user.domain.Profile;
import lombok.Getter;

@Getter
public class UserProfileResponse {
	@NotBlank
	private String introduce;
	private String githubUrl;
	private String blogUrl;
	private String baekjoonId;

	private UserProfileResponse(Profile profile) {
		this.introduce = profile.getIntroduce();
		this.githubUrl = profile.getGithubUrl();
		this.blogUrl = profile.getBlogUrl();
		this.baekjoonId = profile.getBaekjoonId();
	}

	public static UserProfileResponse from(Profile profile) {
		return new UserProfileResponse(profile);
	}

}
