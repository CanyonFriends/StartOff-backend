package kr.startoff.backend.exception.custom;

public class TokenRefreshException extends RuntimeException {
	public TokenRefreshException() {
	}

	public TokenRefreshException(String message) {
		super(message);
	}
}
