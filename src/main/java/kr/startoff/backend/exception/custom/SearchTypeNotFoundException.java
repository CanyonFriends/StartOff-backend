package kr.startoff.backend.exception.custom;

public class SearchTypeNotFoundException extends RuntimeException{
	public SearchTypeNotFoundException() {
		super("존재하지 않는 검색 유형입니다.");
	}
}
