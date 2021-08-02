package kr.startoff.backend.service;

import kr.startoff.backend.entity.User;
import kr.startoff.backend.model.request.SignupRequest;
import kr.startoff.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public User signUp(SignupRequest request){
        User user = User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(encoder.encode(request.getPassword())).build();

        return userRepository.save(user);
    }
}
