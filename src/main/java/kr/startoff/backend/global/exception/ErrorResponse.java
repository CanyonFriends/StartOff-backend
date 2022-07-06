package kr.startoff.backend.global.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ErrorResponse {
	private String message;
	private String code;
	private String status;
	private String timestamp;

	public ErrorResponse(String message, String code, String status) {
		this.message = message;
		this.code = code;
		this.status = status;
		this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	public static ErrorResponse of(ExceptionType exceptionType) {
		return new ErrorResponse(exceptionType.getMessage(), exceptionType.getCode(), exceptionType.getStatus().toString());
	}

	public static ErrorResponse of(String message, ExceptionType exceptionType) {
		return new ErrorResponse(message, exceptionType.getCode(), exceptionType.getStatus().toString());
	}
}
