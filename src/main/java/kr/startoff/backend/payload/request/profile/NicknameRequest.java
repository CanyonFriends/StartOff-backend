package kr.startoff.backend.payload.request.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NicknameRequest {
	private String nickname;

	public NicknameRequest(String nickname) {
		this.nickname = nickname;
	}
}
