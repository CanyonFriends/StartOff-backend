package kr.startoff.backend.domain.user.service;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.startoff.backend.domain.user.domain.AuthProvider;
import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.common.exception.custom.EmailOrNicknameDuplicateException;
import kr.startoff.backend.common.exception.custom.RefreshTokenException;
import kr.startoff.backend.domain.user.dto.request.LoginRequest;
import kr.startoff.backend.domain.user.dto.request.RefreshOrLogoutRequest;
import kr.startoff.backend.domain.user.dto.request.SignupRequest;
import kr.startoff.backend.domain.user.dto.response.AccessTokenResponse;
import kr.startoff.backend.common.dto.CommonResponse;
import kr.startoff.backend.domain.user.dto.response.LoginResponse;
import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.common.security.UserPrincipal;
import kr.startoff.backend.common.security.jwt.JwtUtil;
import kr.startoff.backend.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;
	private final AuthenticationManager authenticationManager;
	private final UserDetailsServiceImpl userDetailsService;

	@Transactional
	public Long signup(SignupRequest request) {
		if (isDuplicateEmail(request.getEmail())) {
			throw new EmailOrNicknameDuplicateException("Email이 중복되었습니다.");
		}
		if (isDuplicateNickname(request.getNickname())) {
			throw new EmailOrNicknameDuplicateException("Nickname이 중복되었습니다.");
		}

		User user = userRepository.save(User.builder()
			.email(request.getEmail())
			.nickname(request.getNickname())
			.password(new BCryptPasswordEncoder().encode(request.getPassword()))
			.provider(AuthProvider.local)
			.build());

		return user.getId();
	}

	@Transactional
	public LoginResponse login(LoginRequest loginRequest) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);

		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
		Long userId = userPrincipal.getId();
		String email = userPrincipal.getEmail();
		String nickname = userPrincipal.getNickname();
		String accessToken = jwtUtil.generateJwtToken(userPrincipal);
		String uuid = UUID.randomUUID().toString();

		redisUtil.setDataExpire(uuid, String.valueOf(userId), (int)JwtUtil.REFRESH_EXPIRATION_SECONDS);

		return LoginResponse.builder()
			.userId(userId)
			.email(email)
			.accessToken(accessToken)
			.nickname(nickname)
			.uuid(uuid)
			.build();
	}

	@Transactional
	public AccessTokenResponse refreshToken(RefreshOrLogoutRequest request) {
		String email = request.getEmail();
		String uuid = request.getUuid();
		String oldAccessToken = request.getAccessToken();

		if (redisUtil.getData(uuid).isEmpty()) {
			throw new RefreshTokenException();
		}

		UserPrincipal userPrincipal = (UserPrincipal)userDetailsService.loadUserByUsername(email);
		String newAccessToken = jwtUtil.generateJwtToken(userPrincipal);

		redisUtil.setDataExpire(oldAccessToken, "true", (int)JwtUtil.TOKEN_EXPIRATION_SECONDS);
		return new AccessTokenResponse(userPrincipal.getId(), newAccessToken);
	}

	@Transactional
	public CommonResponse logout(RefreshOrLogoutRequest request) {
		String uuid = request.getUuid();
		String accessToken = request.getAccessToken();
		if (redisUtil.getData(uuid).isPresent()) {
			redisUtil.deleteData(uuid);
		}
		redisUtil.setDataExpire(accessToken, "true", (int)JwtUtil.TOKEN_EXPIRATION_SECONDS);
		return new CommonResponse(true, "로그아웃");
	}

	private boolean isDuplicateEmail(String email) {
		return userRepository.existsUserByEmail(email);
	}

	private boolean isDuplicateNickname(String nickname) {
		return userRepository.existsUserByNickname(nickname);
	}
}
