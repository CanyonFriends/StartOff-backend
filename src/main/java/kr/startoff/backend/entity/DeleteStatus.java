package kr.startoff.backend.entity;

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
