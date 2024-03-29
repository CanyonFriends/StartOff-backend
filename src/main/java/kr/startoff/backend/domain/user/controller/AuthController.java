package kr.startoff.backend.domain.user.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.startoff.backend.domain.user.dto.request.LoginRequest;
import kr.startoff.backend.domain.user.dto.request.LogoutRequest;
import kr.startoff.backend.domain.user.dto.request.RefreshRequest;
import kr.startoff.backend.domain.user.dto.request.SignupRequest;
import kr.startoff.backend.domain.user.dto.response.AccessTokenResponse;
import kr.startoff.backend.global.common.dto.CommonResponse;
import kr.startoff.backend.domain.user.dto.response.LoginResponse;
import kr.startoff.backend.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<Long> signup(@Valid @RequestBody SignupRequest signupRequest) {
		Long userId = authService.signup(signupRequest);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/users/")
			.buildAndExpand(userId).toUri();

		return ResponseEntity.created(location).body(userId);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		LoginResponse loginResponse = authService.login(loginRequest);
		return ResponseEntity.ok(loginResponse);
	}

	@PostMapping("/refresh")
	public ResponseEntity<AccessTokenResponse> getAccessToken(@RequestBody RefreshRequest request) {
		AccessTokenResponse accessTokenResponse = authService.refreshToken(request);
		return ResponseEntity.ok(accessTokenResponse);
	}

	@PostMapping("/logout")
	public ResponseEntity<CommonResponse> logout(@RequestBody LogoutRequest request) {
		CommonResponse commonResponse = authService.logout(request);
		return ResponseEntity.ok(commonResponse);
	}
}
