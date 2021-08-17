package kr.startoff.backend.security.oauth2;

import static kr.startoff.backend.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.payload.response.JwtResponse;
import kr.startoff.backend.security.UserPrincipal;
import kr.startoff.backend.security.jwt.JwtUtil;
import kr.startoff.backend.util.CookieUtil;
import kr.startoff.backend.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;
	private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		String targetUrl = determineTargetUrl(request, response, authentication);

		if (response.isCommitted()) {
			logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}

		clearAuthenticationAttributes(request, response);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) {
		Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie::getValue);

		if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
			throw new RuntimeException(
				"Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
		}

		String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
		Long userId = userPrincipal.getId();
		String email = userPrincipal.getEmail();
		String nickname = userPrincipal.getNickname();

		String accessToken = jwtUtil.generateJwtToken(userPrincipal);
		String refreshToken = jwtUtil.generateRefreshToken(userPrincipal);
		String uuid = UUID.randomUUID().toString();

		redisUtil.setDataExpire(uuid, refreshToken, JwtUtil.REFRESH_EXPIRATION_SECONDS);

		return UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("access_token", accessToken)
			.queryParam("uuid", uuid)
			.queryParam("user_id", userId.toString())
			.queryParam("email", email)
			.queryParam("nickname", nickname)
			.build().toUriString();
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		super.clearAuthenticationAttributes(request);
		httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	}

	private boolean isAuthorizedRedirectUri(String uri) {
		URI clientRedirectUri = URI.create(uri);
		URI authorizedURI = URI.create("http://localhost:3000/");

		if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
			&& authorizedURI.getPort() == clientRedirectUri.getPort()) {
			return true;
		}
		return false;

	}

}
