package kr.startoff.backend.controller.annotation;

import static kr.startoff.backend.payload.PayloadFixture.*;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import kr.startoff.backend.domain.user.domain.security.UserPrincipal;

public class WithUserPrincipalContextFactory implements WithSecurityContextFactory<WithUserPrincipal> {

	@Override
	public SecurityContext createSecurityContext(WithUserPrincipal annotation) {
		UserPrincipal userPrincipal = UserPrincipal.create(getUser());
		UsernamePasswordAuthenticationToken token =
			new UsernamePasswordAuthenticationToken(userPrincipal, "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(token);
		return context;
	}
}
