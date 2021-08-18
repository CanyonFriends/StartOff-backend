package kr.startoff.backend.controller;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.custom.EmailOrNicknameDuplicateException;
import kr.startoff.backend.exception.custom.InvalidPasswordException;
import kr.startoff.backend.exception.custom.RefreshTokenException;
import kr.startoff.backend.payload.request.LoginRequest;
import kr.startoff.backend.payload.request.RefreshOrLogoutRequest;
import kr.startoff.backend.payload.request.SignupRequest;
import kr.startoff.backend.payload.response.LoginResponse;
import kr.startoff.backend.security.UserPrincipal;
import kr.startoff.backend.security.jwt.JwtUtil;
import kr.startoff.backend.service.UserDetailsServiceImpl;
import kr.startoff.backend.service.UserService;
import kr.startoff.backend.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final UserDetailsServiceImpl userDetailsService;
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;

	@PostMapping("/signup")
	public ResponseEntity<Long> signUp(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userService.isDuplicateEmail(signUpRequest.getEmail())) {
			throw new EmailOrNicknameDuplicateException("Email이 중복되었습니다.");
		}
		if (userService.isDuplicateNickname(signUpRequest.getNickname())) {
			throw new EmailOrNicknameDuplicateException("Nickname이 중복되었습니다.");
		}
		User user = userService.signUp(signUpRequest);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/users/")
			.buildAndExpand(user.getId()).toUri();

		return ResponseEntity.created(location).body(user.getId());
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new InvalidPasswordException();
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
		Long userId = userPrincipal.getId();
		String email = userPrincipal.getEmail();
		String nickname = userPrincipal.getNickname();
		String accessToken = jwtUtil.generateJwtToken(userPrincipal);
		String refreshToken = jwtUtil.generateRefreshToken(userPrincipal);
		String uuid = UUID.randomUUID().toString();

		redisUtil.setDataExpire(uuid, refreshToken, (int)JwtUtil.REFRESH_EXPIRATION_SECONDS);

		LoginResponse jwtResponse = new LoginResponse(accessToken, uuid, userId, email, nickname);

		return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
	}

	@PostMapping("/refresh")
	public ResponseEntity<Void> tokenRefresh(@RequestBody RefreshOrLogoutRequest request,
		HttpServletResponse response) {
		String email = request.getEmail();
		String uuid = request.getUuid();
		String oldAccessToken = request.getAccessToken();
		Optional<String> refreshToken = redisUtil.getData(uuid);

		if (refreshToken.isEmpty()) {
			throw new RefreshTokenException();
		}
		if (!jwtUtil.validateJwtToken(refreshToken.get()) ||
			!email.equals(jwtUtil.getUserNameFromJwtToken(refreshToken.get()))) {
			throw new RefreshTokenException();
		}
		UserPrincipal userPrincipal = (UserPrincipal)userDetailsService.loadUserByUsername(email);
		String newAccessToken = jwtUtil.generateJwtToken(userPrincipal);

		redisUtil.setDataExpire(oldAccessToken, "true", (int)JwtUtil.TOKEN_EXPIRATION_SECONDS);

		response.addHeader("Authorization", "Bearer " + newAccessToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/logout")
	public ResponseEntity<Boolean> logout(RefreshOrLogoutRequest request) {
		String uuid = request.getUuid();
		String accessToken = request.getAccessToken();
		redisUtil.deleteData(uuid);
		redisUtil.setDataExpire(accessToken, "true", (int)JwtUtil.TOKEN_EXPIRATION_SECONDS);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
}
