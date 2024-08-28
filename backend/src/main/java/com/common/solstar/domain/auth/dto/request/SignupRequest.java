package com.common.solstar.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @Email(message = "올바은 이메일 형식이 아닙니다.")
    private String email;

    private String password;

    private String name;

    private String nickname;

    private LocalDate birthDate;

    private String phone;

    private String profileImage;

    private String accountNumber;

    private String bankCode;

    private String accountPassword;

}
