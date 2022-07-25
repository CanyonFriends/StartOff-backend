package kr.startoff.backend.domain.user.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import kr.startoff.backend.domain.user.domain.AuthProvider;
import kr.startoff.backend.domain.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
	@NotBlank
	@Email
	private String email;
	@NotBlank
	private String nickname;
	@NotBlank
	private String password;

	public SignupRequest(String email, String nickname, String password) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
	}

	public User toEntity() {
		return User.builder()
			.email(email)
			.nickname(nickname)
			.password(new BCryptPasswordEncoder().encode(password))
			.provider(AuthProvider.LOCAL)
			.build();
	}
}
