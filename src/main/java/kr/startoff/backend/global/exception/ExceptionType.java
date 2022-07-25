package kr.startoff.backend.global.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionType {
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U-001", "해당 유저는 존재하지 않습니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "U-002", "비밀번호를 확인해주세요."),
	DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "U-003", "닉네임이 중복되었습니다."),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U-004", "이메일이 중복되었습니다."),
	OAUTH2_LOGIN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "U-005", "OAuth2 로그인을 확인해주세요."),
	ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "U-006", "Access Token 이 유효하지 않습니다."),
	REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "U-007", "Refresh Token 이 유효하지 않습니다."),
	OAUTH2_DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U-008", "이미 존재하는 이메일로 OAuth2 로그인을 시도하고 있습니다."),
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CO-001", "해당 댓글이 존재하지 않습니다."),
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "PO-001", "해당 게시글이 존재하지 않습니다."),
	SEARCH_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "PO-002", "해당 유형으로 검색할 수 없습니다."),
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CA-001", "해당 카테고리가 존재하지 않습니다."),
	PROJECT_FORBIDDEN(HttpStatus.FORBIDDEN, "PR-001", "해당 프로젝트에 권한이 존재하지 않습니다."),
	PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "PR-002", "해당 프로젝트는 존재하지 않습니다."),
	TAG_CONFLICT(HttpStatus.CONFLICT, "T-001", "해당 태그는 이미 존재합니다."),
	TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "T-001", "해당 태그는 존재하지 않습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	ExceptionType(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
