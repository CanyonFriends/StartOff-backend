package kr.startoff.backend.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	private ResponseEntity<ErrorInfo> userNotFoundExceptionHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorInfo errorInfo = new ErrorInfo(e.getMessage(), HttpStatus.NOT_FOUND.toString(), request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EmailOrNicknameDuplicateException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	private ResponseEntity<ErrorInfo> emailOrNicknameDuplicateExceptionHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorInfo errorInfo = new ErrorInfo(e.getMessage(), HttpStatus.CONFLICT.toString(), request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidPasswordException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private ResponseEntity<ErrorInfo> invalidPasswordExceptionHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorInfo errorInfo = new ErrorInfo(e.getMessage(), HttpStatus.BAD_REQUEST.toString(), request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TokenRefreshException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private ResponseEntity<ErrorInfo> tokenRefreshExceptionHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorInfo errorInfo = new ErrorInfo(e.getMessage(), HttpStatus.BAD_REQUEST.toString(), request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}
}
