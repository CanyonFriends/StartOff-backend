package kr.startoff.backend.domain.user.dto.request;

import kr.startoff.backend.domain.user.domain.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileRequest {
	private String githubUrl;
	private String blogUrl;
	private String baekjoonId;
	private String introduce;

	public ProfileRequest(String githubUrl, String blogUrl, String baekjoonId, String introduce) {
		this.githubUrl = githubUrl;
		this.blogUrl = blogUrl;
		this.baekjoonId = baekjoonId;
		this.introduce = introduce;
	}

	public Profile toEntity() {
		return Profile.builder()
			.githubUrl(githubUrl)
			.blogUrl(blogUrl)
			.baekjoonId(baekjoonId)
			.introduce(introduce)
			.build();
	}
}
