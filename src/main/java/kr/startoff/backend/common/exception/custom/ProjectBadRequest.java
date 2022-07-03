package kr.startoff.backend.common.exception.custom;

public class ProjectBadRequest extends RuntimeException {
	public ProjectBadRequest() {
		super("잘못된 요청입니다.");
	}
}
