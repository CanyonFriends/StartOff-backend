package kr.startoff.backend.global.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.startoff.backend.global.exception.custom.AccessTokenException;
import kr.startoff.backend.global.security.jwt.CustomStatus;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws RuntimeException {
		log.debug("JwtInterceptor.preHandle");
		if (response.getStatus() == CustomStatus.IS_LOCKED_TOKEN.getCode()) {
			throw new AccessTokenException(CustomStatus.IS_LOCKED_TOKEN.toString());
		} else if (response.getStatus() == CustomStatus.INVALID_TOKEN.getCode()) {
			throw new AccessTokenException(CustomStatus.INVALID_TOKEN.toString());
		}
		return true;
	}
}
