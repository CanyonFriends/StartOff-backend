package kr.startoff.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import kr.startoff.backend.global.security.jwt.AuthTokenFilter;
import kr.startoff.backend.global.security.jwt.JwtAccessDeniedHandler;
import kr.startoff.backend.global.security.jwt.JwtAuthEntryPoint;
import kr.startoff.backend.global.security.jwt.JwtUtil;
import kr.startoff.backend.domain.user.service.CustomOAuth2UserService;
import kr.startoff.backend.domain.user.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import kr.startoff.backend.global.security.oauth2.OAuth2AuthenticationFailureHandler;
import kr.startoff.backend.global.security.oauth2.OAuth2AuthenticationSuccessHandler;
import kr.startoff.backend.domain.user.service.UserDetailsServiceImpl;
import kr.startoff.backend.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsServiceImpl userDetailsService;
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;
	private final JwtAuthEntryPoint unauthorizedHandler;
	private final JwtAccessDeniedHandler accessDeniedHandler;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

	/*
	  By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
	  the authorization request. But, since our service is stateless, we can't save it in
	  the session. We'll save the request in a Base64 encoded cookie instead.
	*/
	@Bean
	public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
		return new HttpCookieOAuth2AuthorizationRequestRepository();
	}

	@Bean
	public AuthTokenFilter authTokenFilter() {
		return new AuthTokenFilter(jwtUtil, redisUtil, userDetailsService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(WebSecurity web) {
		web
			.ignoring()
			.antMatchers(
				"/h2-console/**",
				"/favicon.ico",
				"/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html",
				"/webjars/**", "/swagger/**"
			);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.formLogin()
			.disable()
			.httpBasic()
			.disable()
			.exceptionHandling()
			.authenticationEntryPoint(unauthorizedHandler)
			.accessDeniedHandler(accessDeniedHandler)
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.oauth2Login()
			.authorizationEndpoint()
			.baseUri("/oauth2/authorize")
			.authorizationRequestRepository(cookieAuthorizationRequestRepository())
			.and()
			.userInfoEndpoint()
			.userService(customOAuth2UserService)
			.and()
			.successHandler(oAuth2AuthenticationSuccessHandler)
			.failureHandler(oAuth2AuthenticationFailureHandler);

		http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}
