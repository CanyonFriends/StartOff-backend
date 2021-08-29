package kr.startoff.backend.exception.custom;

public class EmailOrNicknameDuplicateException extends RuntimeException{
	public EmailOrNicknameDuplicateException() {
	}

	public EmailOrNicknameDuplicateException(String message) {
		super(message);
	}
}
