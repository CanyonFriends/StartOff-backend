package kr.startoff.backend.security.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import kr.startoff.backend.service.UserDetailsServiceImpl;
import kr.startoff.backend.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		try {
			Optional<String> jwt = parseJwt(request);
			if (jwt.isPresent()) {
				if (jwtUtil.validateJwtToken(jwt.get())) {
					Optional<String> isLockedAccessToken = redisUtil.getData(jwt.get());
					if (isLockedAccessToken.isEmpty()) {
						String username = jwtUtil.getUserNameFromJwtToken(jwt.get());

						SecurityContext context = SecurityContextHolder.createEmptyContext();

						UserDetails userDetails = userDetailsService.loadUserByUsername(username);
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						context.setAuthentication(authentication);
						SecurityContextHolder.setContext(context);
					} else {
						response.setStatus(CustomStatus.IS_LOCKED_TOKEN.getCode());
					}
				} else {
					response.setStatus(CustomStatus.INVALID_TOKEN.getCode());
				}
			}
		} catch (ExpiredJwtException e) {
			log.error("ExpiredJwtException : {}", e.getMessage());
		} catch (Exception e) {
			log.error("Cannot set user authentication: {}", e.getMessage());
		}

		filterChain.doFilter(request, response);
	}

	private Optional<String> parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return Optional.of(headerAuth.substring(7));
		}
		return Optional.empty();
	}
}
