package kr.startoff.backend.domain.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NicknameRequest {
	String nickname;

	public NicknameRequest(String nickname) {
		this.nickname = nickname;
	}
}
