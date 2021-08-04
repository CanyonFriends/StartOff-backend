package kr.startoff.backend.controller;

import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.TokenRefreshException;
import kr.startoff.backend.model.request.LoginRequest;
import kr.startoff.backend.model.request.SignupRequest;
import kr.startoff.backend.model.response.JwtResponse;
import kr.startoff.backend.security.jwt.JwtUtils;
import kr.startoff.backend.service.UserDetailsServiceImpl;
import kr.startoff.backend.service.UserService;
import kr.startoff.backend.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;
    private final RedisUtil redisUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequest signUpRequest) {
        User user = userService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtUtils.generateJwtToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        redisUtil.setDataExpire(user.getUsername(), refreshToken, JwtUtils.REFRESH_EXPIRATION_SECONDS);

        Cookie cookie = convertRefreshTokenToCookie(refreshToken);
        response.addCookie(cookie);
        response.addHeader("Authorization", "Bearer " + accessToken);
        JwtResponse jwtResponse = new JwtResponse(accessToken,refreshToken, user.getId(), user.getEmail());

        return new ResponseEntity<>(jwtResponse,HttpStatus.OK);
    }

    private Cookie convertRefreshTokenToCookie(String refreshToken) {
        Cookie cookie = new Cookie("refresh", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)JwtUtils.REFRESH_EXPIRATION_SECONDS);
        cookie.setPath("/");
        return cookie;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> tokenRefresh(HttpServletRequest request, HttpServletResponse response){
        Cookie refreshCookie = getRefreshTokenAtCookie(request).orElseThrow(
                TokenRefreshException::new);

        String refreshToken = refreshCookie.getValue();
        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
        if(redisUtil.getData(username).isEmpty()){
            throw new TokenRefreshException();
        }
        User user = (User) userDetailsService.loadUserByUsername(username);

        String accessToken = jwtUtils.generateJwtToken(user);
        response.addHeader("Authorization","Bearer "+accessToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Optional<Cookie> getRefreshTokenAtCookie(HttpServletRequest request){
        final Cookie[] cookies = request.getCookies();
        if(cookies==null) return Optional.empty();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh"))
                return Optional.of(cookie);
        }
        return Optional.empty();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        Cookie refreshCookie = getRefreshTokenAtCookie(request).orElseThrow(
                TokenRefreshException::new);

        String refreshToken = refreshCookie.getValue();
        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
        redisUtil.deleteData(username);

        return new ResponseEntity<>(true,HttpStatus.OK);
    }
}
