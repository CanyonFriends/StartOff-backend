package kr.startoff.backend.domain.user.domain.oauth2;

import static kr.startoff.backend.global.exception.ExceptionType.*;

import java.util.Map;

import kr.startoff.backend.domain.user.domain.AuthProvider;
import kr.startoff.backend.domain.user.exception.UserException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2UserInfoFactory {
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		if (registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())) {
			return new GithubOAuth2UserInfo(attributes);
		} else {
			throw new UserException(OAUTH2_NOT_SUPPORTED);
		}
	}
}
