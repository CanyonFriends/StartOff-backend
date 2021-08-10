package kr.startoff.backend.controller;

import java.net.URI;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.TokenRefreshException;
import kr.startoff.backend.model.request.LoginRequest;
import kr.startoff.backend.model.request.SignupRequest;
import kr.startoff.backend.model.response.JwtResponse;
import kr.startoff.backend.security.UserPrincipal;
import kr.startoff.backend.security.jwt.JwtUtil;
import kr.startoff.backend.service.UserDetailsServiceImpl;
import kr.startoff.backend.service.UserService;
import kr.startoff.backend.util.CookieUtil;
import kr.startoff.backend.util.RedisUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final UserDetailsServiceImpl userDetailsService;
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;
	private final CookieUtil cookieUtil;

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequest signUpRequest) {
		User user = userService.signUp(signUpRequest);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/users/")
			.buildAndExpand(user.getId()).toUri();

		return ResponseEntity.created(location).body(user.getId());
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		String accessToken = jwtUtil.generateJwtToken(userPrincipal);
		String refreshToken = jwtUtil.generateRefreshToken(userPrincipal);

		redisUtil.setDataExpire(userPrincipal.getUsername(), refreshToken, JwtUtil.REFRESH_EXPIRATION_SECONDS);

		CookieUtil.addCookie(response,"Authorization",accessToken,(int)JwtUtil.TOKEN_EXPIRATION_SECONDS);
		CookieUtil.addCookie(response,"refresh",refreshToken,(int)JwtUtil.REFRESH_EXPIRATION_SECONDS);
		JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken, userPrincipal.getId(), userPrincipal.getEmail());

		return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> tokenRefresh(HttpServletRequest request, HttpServletResponse response) {
		Cookie refreshCookie = getRefreshTokenAtCookie(request).orElseThrow(
			TokenRefreshException::new);

		String refreshToken = refreshCookie.getValue();
		String username = jwtUtil.getUserNameFromJwtToken(refreshToken);
		if (redisUtil.getData(username).isEmpty()) {
			throw new TokenRefreshException();
		}
		UserPrincipal userPrincipal = (UserPrincipal)userDetailsService.loadUserByUsername(username);

		String accessToken = jwtUtil.generateJwtToken(userPrincipal);
		response.addHeader("Authorization", "Bearer " + accessToken);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private Optional<Cookie> getRefreshTokenAtCookie(HttpServletRequest request) {
		final Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return Optional.empty();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("refresh"))
				return Optional.of(cookie);
		}
		return Optional.empty();
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		Cookie refreshCookie = getRefreshTokenAtCookie(request).orElseThrow(
			TokenRefreshException::new);

		String refreshToken = refreshCookie.getValue();
		String username = jwtUtil.getUserNameFromJwtToken(refreshToken);
		redisUtil.deleteData(username);

		return new ResponseEntity<>(true, HttpStatus.OK);
	}
}
