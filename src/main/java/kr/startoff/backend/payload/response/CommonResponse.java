package kr.startoff.backend.payload.response;

import lombok.Getter;

@Getter
public class CommonResponse {
	private boolean success;
	private String msg;

	public CommonResponse(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}
}
