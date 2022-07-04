package kr.startoff.backend.interceptor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import kr.startoff.backend.global.exception.custom.AccessTokenException;
import kr.startoff.backend.global.interceptor.JwtInterceptor;
import kr.startoff.backend.global.security.jwt.CustomStatus;

@ExtendWith(MockitoExtension.class)
class JwtInterceptorTest {

	private MockHttpServletRequest mockHttpServletRequest;
	private MockHttpServletResponse mockHttpServletResponse;
	private JwtInterceptor jwtInterceptor;

	@BeforeEach
	void setUp() {
		mockHttpServletRequest = new MockHttpServletRequest();
		mockHttpServletResponse = new MockHttpServletResponse();
		jwtInterceptor = new JwtInterceptor();
	}

	@Test
	void preHandleTestWithInvalidToken() throws Exception {
		mockHttpServletResponse.setStatus(CustomStatus.INVALID_TOKEN.getCode());

		assertThrows(AccessTokenException.class,
			() -> jwtInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, null));
	}

	@Test
	void preHandleTestWithLockedToken() throws Exception {
		mockHttpServletResponse.setStatus(CustomStatus.IS_LOCKED_TOKEN.getCode());

		assertThrows(AccessTokenException.class,
			() -> jwtInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, null));
	}

	@Test
	void preHandleTestWithSuccess() throws Exception {
		assertTrue(jwtInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, null));
	}
}