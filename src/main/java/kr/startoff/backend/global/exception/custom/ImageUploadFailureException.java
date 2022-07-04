package kr.startoff.backend.global.exception.custom;

public class ImageUploadFailureException extends RuntimeException {
	public ImageUploadFailureException() {
		super("업로드를 실패 하였습니다.");
	}
}
