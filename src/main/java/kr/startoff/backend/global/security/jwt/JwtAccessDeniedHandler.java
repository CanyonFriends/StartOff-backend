package kr.startoff.backend.global.security.jwt;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.global.exception.ErrorResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request,
		HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.FORBIDDEN.value());

		try (OutputStream os = response.getOutputStream()) {
			ObjectMapper objectMapper = new ObjectMapper();
			ErrorResponse errorInfo = new ErrorResponse("접근할 권한이 부족합니다.",
				HttpStatus.FORBIDDEN.toString(), request.getRequestURI());
			objectMapper.writeValue(os, errorInfo);
			os.flush();
		}
	}
}
