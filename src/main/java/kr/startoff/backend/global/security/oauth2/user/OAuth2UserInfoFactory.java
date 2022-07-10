package kr.startoff.backend.global.security.oauth2.user;

import static kr.startoff.backend.global.exception.ExceptionType.*;

import java.util.Map;

import kr.startoff.backend.domain.user.domain.AuthProvider;
import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.global.exception.ExceptionType;

public class OAuth2UserInfoFactory {
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
			return new GithubOAuth2UserInfo(attributes);
		} else {
			throw new UserException(OAUTH2_LOGIN_UNAUTHORIZED);
			// throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
		}
	}
}
