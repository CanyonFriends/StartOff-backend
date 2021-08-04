package kr.startoff.backend.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.startoff.backend.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils  {
    @Value("${jwt.secret-key}")
    private String secretKey;
    public final static long TOKEN_EXPIRATION_SECONDS = 1000L * 10;
    public final static long REFRESH_EXPIRATION_SECONDS = 1000L * 60 * 24 * 2;

    private Key key;

    @PostConstruct
    public void createKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(User user) {
        return doGenerateToken(user,TOKEN_EXPIRATION_SECONDS);
    }
    public String generateRefreshToken(User user) {
        return doGenerateToken(user,REFRESH_EXPIRATION_SECONDS);
    }
    private String doGenerateToken(User user,long expirationMs){
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject((user.getUsername()))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key,SignatureAlgorithm.HS512)
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
