package kr.startoff.backend.common.exception.custom;

public class PostNotFoundException extends RuntimeException{
	public PostNotFoundException() {
		super("해당 글을 찾을 수 없습니다.");
	}
}
