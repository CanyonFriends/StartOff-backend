package kr.startoff.backend.controller;

import kr.startoff.backend.entity.User;
import kr.startoff.backend.model.request.SignupRequest;
import kr.startoff.backend.security.jwt.JwtUtils;
import kr.startoff.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupRequest signUpRequest) {
        User user = userService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user.getId());
    }
}
