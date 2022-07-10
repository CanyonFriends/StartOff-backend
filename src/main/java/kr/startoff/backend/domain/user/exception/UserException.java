package kr.startoff.backend.domain.user.exception;

import kr.startoff.backend.global.exception.BaseException;
import kr.startoff.backend.global.exception.ExceptionType;

public class UserException extends BaseException {
	public UserException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
