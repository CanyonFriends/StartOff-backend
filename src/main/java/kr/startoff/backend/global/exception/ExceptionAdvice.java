package kr.startoff.backend.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(BaseException.class)
	private ResponseEntity<ErrorResponse> handleBaseException(final BaseException e) {
		ExceptionType exceptionType = e.getErrorCode();
		ErrorResponse errorResponse = ErrorResponse.of(exceptionType);
		return new ResponseEntity<>(errorResponse, exceptionType.getStatus());
	}

	// @ExceptionHandler(BindException.class)
	// private ResponseEntity<ErrorResponse> handleBindException(final BindException e) {
	// 	ErrorCode errorCode = ErrorCode.INVALID_INPUT;
	// 	ErrorResponse errorResponse = ErrorResponse.of(e.getBindingResult(), errorCode);
	// 	return new ResponseEntity<>(errorResponse, e.)
	// }
}
