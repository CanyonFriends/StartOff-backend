package kr.startoff.backend.global.exception;

public abstract class BaseException extends RuntimeException {
	private final ExceptionType exceptionType;

	protected BaseException(ExceptionType exceptionType) {
		super(exceptionType.getMessage());
		this.exceptionType = exceptionType;
	}

	public ExceptionType getErrorCode() {
		return exceptionType;
	}
}
