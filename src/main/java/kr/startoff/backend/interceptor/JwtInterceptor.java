package kr.startoff.backend.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.startoff.backend.exception.custom.TokenRefreshException;
import kr.startoff.backend.security.jwt.CustomStatus;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws RuntimeException {
		log.debug("JwtInterceptor.preHandle");
		if (response.getStatus() == CustomStatus.IS_LOCKED_TOKEN.getCode()) {
			throw new TokenRefreshException("유효하지 않은 AccessToken 입니다. : " + CustomStatus.IS_LOCKED_TOKEN);
		} else if (response.getStatus() == CustomStatus.INVALID_TOKEN.getCode()) {
			throw new TokenRefreshException("유효하지 않은 AccessToken 입니다. : " + CustomStatus.INVALID_TOKEN);
		}
		return true;
	}
}
