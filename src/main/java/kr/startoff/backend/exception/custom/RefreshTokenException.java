package kr.startoff.backend.exception.custom;

public class RefreshTokenException extends RuntimeException {
	public RefreshTokenException() {
		super("유효하지 않은 RefreshToken 입니다.");
	}

}