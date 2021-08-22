package kr.startoff.backend.payload.request.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IntroduceRequest {
	private String introduce;

	public IntroduceRequest(String introduce) {
		this.introduce = introduce;
	}
}
