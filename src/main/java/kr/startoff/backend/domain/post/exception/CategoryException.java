package kr.startoff.backend.domain.post.exception;

import kr.startoff.backend.global.exception.BaseException;
import kr.startoff.backend.global.exception.ExceptionType;

public class CategoryException extends BaseException {
	public CategoryException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
