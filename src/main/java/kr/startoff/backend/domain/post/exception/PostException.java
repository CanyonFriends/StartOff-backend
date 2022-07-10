package kr.startoff.backend.domain.post.exception;

import kr.startoff.backend.global.exception.BaseException;
import kr.startoff.backend.global.exception.ExceptionType;

public class PostException extends BaseException {
	public PostException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
