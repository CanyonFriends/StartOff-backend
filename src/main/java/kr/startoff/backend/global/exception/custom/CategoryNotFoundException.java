package kr.startoff.backend.global.exception.custom;

public class CategoryNotFoundException extends RuntimeException{
	public CategoryNotFoundException() {
		super("해당 카테고리를 찾을 수 없습니다.");
	}
}
