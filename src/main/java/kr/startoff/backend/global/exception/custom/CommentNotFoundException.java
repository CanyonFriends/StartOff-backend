package kr.startoff.backend.global.exception.custom;

public class CommentNotFoundException extends RuntimeException{
	public CommentNotFoundException() {
		super("해당 댓글이 존재하지 않습니다.");
	}
}
