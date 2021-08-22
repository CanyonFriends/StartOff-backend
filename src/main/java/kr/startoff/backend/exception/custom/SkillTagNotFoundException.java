package kr.startoff.backend.exception.custom;

public class SkillTagNotFoundException extends RuntimeException{
	public SkillTagNotFoundException() {
		super("해당 스킬태그를 찾을 수 없습니다.");
	}
}
