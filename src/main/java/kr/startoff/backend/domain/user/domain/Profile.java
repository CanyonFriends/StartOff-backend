package kr.startoff.backend.domain.user.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Profile {
	@Column(name = "github_url")
	private String githubUrl = "";

	@Column(name = "blog_url")
	private String blogUrl = "";

	@Column(name = "baekjoon_id")
	private String baekjoonId = "";

	@Column(name = "introduce")
	private String introduce = "";

	@Builder
	public Profile(String githubUrl, String blogUrl, String baekjoonId, String introduce) {
		this.githubUrl = githubUrl;
		this.blogUrl = blogUrl;
		this.baekjoonId = baekjoonId;
		this.introduce = introduce;
	}
}
