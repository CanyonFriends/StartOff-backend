package kr.startoff.backend.domain.tag.exception;

import kr.startoff.backend.global.exception.BaseException;
import kr.startoff.backend.global.exception.ExceptionType;

public class TagException extends BaseException {
	public TagException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
