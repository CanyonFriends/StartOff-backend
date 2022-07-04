package kr.startoff.backend.global.exception.custom;

public class ProjectBadRequest extends RuntimeException {
	public ProjectBadRequest() {
		super("잘못된 요청입니다.");
	}
}
