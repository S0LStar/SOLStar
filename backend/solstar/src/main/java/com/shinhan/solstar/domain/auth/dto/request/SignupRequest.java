package com.shinhan.solstar.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
