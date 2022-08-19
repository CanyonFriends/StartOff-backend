package kr.startoff.backend.domain.user.service;

import static kr.startoff.backend.global.exception.ExceptionType.*;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import kr.startoff.backend.domain.user.exception.UserException;
import kr.startoff.backend.domain.user.domain.oauth2.OAuth2UserInfoFactory;
import kr.startoff.backend.domain.user.domain.AuthProvider;
import kr.startoff.backend.domain.user.domain.User;
import kr.startoff.backend.domain.user.repository.UserRepository;
import kr.startoff.backend.domain.user.domain.security.UserPrincipal;
import kr.startoff.backend.domain.user.domain.oauth2.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOAuth2UserService = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(oAuth2UserRequest);
		return processOAuth2User(oAuth2UserRequest, oAuth2User);
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
			oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
		if (oAuth2UserInfo.getEmail().isEmpty()) {
			throw new UserException(OAUTH2_LOGIN_UNAUTHORIZED);
		}

		Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (!user.getProvider().equals(AuthProvider.valueOf(getAuthProvider(oAuth2UserRequest)))) {
				throw new UserException(OAUTH2_DUPLICATE_EMAIL);
			}
			return UserPrincipal.create(user, oAuth2User.getAttributes());
		}
		return UserPrincipal.create(registerNewUser(oAuth2UserRequest, oAuth2UserInfo), oAuth2User.getAttributes());
	}

	private String getAuthProvider(OAuth2UserRequest oAuth2UserRequest) {
		return oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase();
	}

	private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
		String email = oAuth2UserInfo.getEmail();
		String nickname = oAuth2UserInfo.getName();

		User user = User.builder()
			.email(email)
			.password("")
			.nickname(userRepository.existsUserByNickname(nickname) ? addRandomString(nickname) : nickname)
			.provider(AuthProvider.valueOf(getAuthProvider(oAuth2UserRequest)))
			.build();

		return userRepository.save(user);
	}

	private String addRandomString(String nickname) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		return nickname + random.nextInt(1000, 10000);
	}
}
