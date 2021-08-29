package kr.startoff.backend.exception.custom;

public class ProjectBadRequest extends RuntimeException {
	public ProjectBadRequest() {
		super("잘못된 요청입니다.");
	}
}
