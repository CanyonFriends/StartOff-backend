package kr.startoff.backend.domain.comment.exception;

import kr.startoff.backend.global.exception.BaseException;
import kr.startoff.backend.global.exception.ExceptionType;

public class CommentException extends BaseException {
	public CommentException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
