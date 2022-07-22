package kr.startoff.backend.domain.user.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	private Long id;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "nickname", nullable = false, unique = true)
	private String nickname;

	@Column(name = "image_url")
	private String imageUrl;

	@Embedded
	private Profile profile = new Profile();

	@Enumerated(EnumType.STRING)
	@Column(name = "provider", nullable = false)
	private AuthProvider provider;

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

	public void updateImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void updateProfile(Profile profile) {
		this.profile = profile;
	}
}
