package kr.startoff.backend.exception.custom;

public class InvalidPasswordException extends RuntimeException {
	public InvalidPasswordException() {
		super("비밀번호를 확인해주세요.");
	}
}
