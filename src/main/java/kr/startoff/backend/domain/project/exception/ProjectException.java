package kr.startoff.backend.domain.project.exception;

import kr.startoff.backend.global.exception.BaseException;
import kr.startoff.backend.global.exception.ExceptionType;

public class ProjectException extends BaseException {
	public ProjectException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}
