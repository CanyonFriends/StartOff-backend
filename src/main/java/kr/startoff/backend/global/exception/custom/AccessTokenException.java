package kr.startoff.backend.global.exception.custom;

public class AccessTokenException extends RuntimeException {
	public AccessTokenException() {
	}

	public AccessTokenException(String message) {
		super("유효하지 않은 AccessToken 입니다. : " + message);
	}
}
