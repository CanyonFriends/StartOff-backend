package kr.startoff.backend.global.security.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.startoff.backend.domain.user.domain.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	public final static long TOKEN_EXPIRATION_SECONDS = 1000L * 60 * 10;
	public final static long REFRESH_EXPIRATION_SECONDS = 1000L * 60 * 120;
	private final Key key;

	public JwtUtil(@Value("${jwt.secret-key}") String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateJwtToken(UserPrincipal userPrincipal) {
		return doGenerateToken(userPrincipal, TOKEN_EXPIRATION_SECONDS);
	}

	public String generateRefreshToken(UserPrincipal userPrincipal) {
		return doGenerateToken(userPrincipal, REFRESH_EXPIRATION_SECONDS);
	}

	private String doGenerateToken(UserPrincipal user, long expirationMs) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + expirationMs);
		if (expirationMs == TOKEN_EXPIRATION_SECONDS) {
			log.debug("AccessToken : " + now + "\n" + validity);
		} else if (expirationMs == REFRESH_EXPIRATION_SECONDS) {
			log.debug("RefreshToken : " + now + "\n" + validity);
		}
		return Jwts.builder()
			.setSubject((user.getUsername()))
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
			return true;
		} catch (SecurityException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}
}
