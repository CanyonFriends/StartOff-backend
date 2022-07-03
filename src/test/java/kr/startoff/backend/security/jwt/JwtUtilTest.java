package kr.startoff.backend.security.jwt;

import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import java.security.SecureRandom;
import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kr.startoff.backend.common.security.UserPrincipal;
import kr.startoff.backend.common.security.jwt.JwtUtil;

class JwtUtilTest {
	private JwtUtil jwtUtil;

	@BeforeEach
	void setUp() {
		jwtUtil = new JwtUtil(generateSafeToken());
	}

	private String generateSafeToken() {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[64];
		random.nextBytes(bytes);
		return Base64.getEncoder().encodeToString(bytes);
	}

	@Test
	void generateJwtTokenTest() throws Exception {
		//given
		UserPrincipal userPrincipal = UserPrincipal.create(getUser());
		final String token = jwtUtil.generateJwtToken(userPrincipal);

		assertNotNull(token);
		assertEquals(3, token.split("\\.").length);
	}

	@Test
	void generateRefreshTokenTest() throws Exception {
		//given
		UserPrincipal userPrincipal = UserPrincipal.create(getUser());
		final String token = jwtUtil.generateRefreshToken(userPrincipal);

		assertNotNull(token);
		assertEquals(3, token.split("\\.").length);
	}

	@Test
	void getUserNameFromJwtTokenTest() {
		UserPrincipal userPrincipal = UserPrincipal.create(getUser());
		final String token = jwtUtil.generateJwtToken(userPrincipal);
		String username = jwtUtil.getUserNameFromJwtToken(token);

		assertEquals(userPrincipal.getUsername(), username);
	}
	
	@Test
	void validateJwtTokenTest() throws Exception{
		UserPrincipal userPrincipal = UserPrincipal.create(getUser());
		final String token = jwtUtil.generateJwtToken(userPrincipal);

		assertTrue(jwtUtil.validateJwtToken(token));
	}

	@Test
	void validateJwtThrowExceptionTest() throws Exception{
		assertFalse(jwtUtil.validateJwtToken("Invalid Token"));
	}
}