package kr.startoff.backend.model.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SignupRequest {
    @NotBlank
    String email;
    @NotBlank
    String nickname;
    @NotBlank
    String password;
}
