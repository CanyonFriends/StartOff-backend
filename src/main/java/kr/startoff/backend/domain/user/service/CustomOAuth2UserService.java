package kr.startoff.backend.domain.user.service;

import static kr.startoff.backend.global.exception.ExceptionType.*;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
		try {
			return processOAuth2User(oAuth2UserRequest, oAuth2User);
		} catch (AuthenticationException ex) {
			throw ex;
		} catch (Exception ex) {
			// Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
			oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
		if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			throw new UserException(OAUTH2_LOGIN_UNAUTHORIZED);
			// throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}

		Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
		User user;
		if (userOptional.isPresent()) {
			user = userOptional.get();
			if (!user.getProvider()
				.equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
				throw new UserException(OAUTH2_LOGIN_UNAUTHORIZED);
				// throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " + user.getProvider() + " account. Please use your " + user.getProvider() + " account to login.");
			}
			user = updateExistingUser(user, oAuth2UserInfo);
		} else {
			user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
		}

		return UserPrincipal.create(user, oAuth2User.getAttributes());
	}

	private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
		User user = User.builder()
			.email(oAuth2UserInfo.getEmail())
			.password("")
			.provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
			.build();

		StringBuilder nickname = new StringBuilder(oAuth2UserInfo.getName());
		while (userRepository.existsUserByNickname(nickname.toString())) {
			nickname.append(ThreadLocalRandom.current().nextInt(0, 9));
		}
		user.updateImageUrl(oAuth2UserInfo.getImageUrl());
		user.updateNickname(nickname.toString());
		return userRepository.save(user);
	}

	private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
		existingUser.updateImageUrl(oAuth2UserInfo.getImageUrl());
		return userRepository.save(existingUser);
	}

}
