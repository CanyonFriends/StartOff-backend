package kr.startoff.backend.domain.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	Long id;

	@Column(name = "password", nullable = false)
	String password;

	@Column(name = "email", nullable = false, unique = true)
	String email;

	@Column(name = "nickname", nullable = false, unique = true)
	String nickname;

	@Column(name = "github_url")
	String githubUrl = "";

	@Column(name = "blog_url")
	String blogUrl = "";

	@Column(name = "baekjoon_id")
	String baekjoonId = "";

	@Column(name = "introduce")
	String introduce = "";

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "provider")
	AuthProvider provider;

	@Column(name = "provider_id")
	String providerId;

	@Column(name = "image_url")
	String imageUrl = "";

	@Builder
	public User(String email, String nickname, String password, AuthProvider provider) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
		this.provider = provider;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updateGithubUrl(String githubUrl) {
		this.githubUrl = githubUrl;
	}

	public void updateBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl;
	}

	public void updateBaekjoonId(String baekjoonId) {
		this.baekjoonId = baekjoonId;
	}

	public void updateIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public void updateImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
