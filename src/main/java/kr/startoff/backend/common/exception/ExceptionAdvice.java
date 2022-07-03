package kr.startoff.backend.common.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.startoff.backend.common.exception.custom.AccessTokenException;
import kr.startoff.backend.common.exception.custom.CategoryNotFoundException;
import kr.startoff.backend.common.exception.custom.CommentNotFoundException;
import kr.startoff.backend.common.exception.custom.EmailOrNicknameDuplicateException;
import kr.startoff.backend.common.exception.custom.ImageUploadFailureException;
import kr.startoff.backend.common.exception.custom.InvalidPasswordException;
import kr.startoff.backend.common.exception.custom.PostNotFoundException;
import kr.startoff.backend.common.exception.custom.ProjectBadRequest;
import kr.startoff.backend.common.exception.custom.ProjectNotFoundException;
import kr.startoff.backend.common.exception.custom.RefreshTokenException;
import kr.startoff.backend.common.exception.custom.SearchTypeNotFoundException;
import kr.startoff.backend.common.exception.custom.SkillTagBadRequest;
import kr.startoff.backend.common.exception.custom.SkillTagNotFoundException;
import kr.startoff.backend.common.exception.custom.UserNotFoundException;

@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler({UserNotFoundException.class, ProjectNotFoundException.class, SkillTagNotFoundException.class,
		PostNotFoundException.class, CategoryNotFoundException.class, CommentNotFoundException.class,
		SearchTypeNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	private ResponseEntity<ErrorInfo> notFoundErrorHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorInfo errorInfo = new ErrorInfo(e.getMessage(), HttpStatus.NOT_FOUND.toString(), request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EmailOrNicknameDuplicateException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	private ResponseEntity<ErrorInfo> conflictErrorHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorInfo errorInfo = new ErrorInfo(e.getMessage(), HttpStatus.CONFLICT.toString(), request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
	}

	@ExceptionHandler({InvalidPasswordException.class, ProjectBadRequest.class, SkillTagBadRequest.class,
		ImageUploadFailureException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private ResponseEntity<ErrorInfo> badRequestErrorHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorInfo errorInfo = new ErrorInfo(e.getMessage(), HttpStatus.BAD_REQUEST.toString(), request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({AccessTokenException.class, RefreshTokenException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	private ResponseEntity<ErrorInfo> unauthorizedErrorHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorInfo errorInfo = new ErrorInfo(e.getMessage(), HttpStatus.UNAUTHORIZED.toString(),
			request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
	}
}
