package kr.startoff.backend.prototype;

import org.springframework.security.crypto.password.PasswordEncoder;

import kr.startoff.backend.entity.AuthProvider;
import kr.startoff.backend.entity.User;
import kr.startoff.backend.payload.request.LoginRequest;
import kr.startoff.backend.payload.request.SignupRequest;
import kr.startoff.backend.payload.request.UserPasswordChangeRequest;
import kr.startoff.backend.payload.response.UserInfoResponse;

public class UserPrototype {
	public static final Long USER_ID = 1L;
	public static final String EMAIL = "proto_seo@naver.com";
	public static final String NICKNAME = "proto_seo";
	public static final String PASSWORD = "password";

	public static User user() {
		return User.builder()
			.email(EMAIL)
			.nickname(NICKNAME)
			.password(PASSWORD)
			.provider(AuthProvider.local)
			.build();
	}

	public static User user(PasswordEncoder encoder) {
		return User.builder()
			.email(EMAIL)
			.nickname(NICKNAME)
			.password(encoder.encode(PASSWORD))
			.provider(AuthProvider.local)
			.build();
	}

	public static LoginRequest loginRequest() {
		return new LoginRequest(EMAIL, PASSWORD);
	}

	public static SignupRequest signupRequest() {
		return new SignupRequest(EMAIL, NICKNAME, PASSWORD);
	}

	public static UserPasswordChangeRequest userPasswordChangeRequest() {
		return new UserPasswordChangeRequest(PASSWORD, "afterPassword");
	}

	public static UserInfoResponse userInfo() {
		return new UserInfoResponse(EMAIL,NICKNAME);
	}
}
