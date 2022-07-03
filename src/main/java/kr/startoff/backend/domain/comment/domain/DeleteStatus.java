package kr.startoff.backend.domain.comment.domain;

public enum DeleteStatus {
	Y(true), N(false);

	private boolean value;

	DeleteStatus(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return value;
	}
}
