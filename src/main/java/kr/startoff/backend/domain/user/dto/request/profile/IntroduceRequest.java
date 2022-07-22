package kr.startoff.backend.domain.user.dto.request.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NicknameAndIntroduceRequest {
	private String nickname;
	private String introduce;

	public NicknameAndIntroduceRequest(String nickname, String introduce) {
		this.nickname = nickname;
		this.introduce = introduce;
	}
}
