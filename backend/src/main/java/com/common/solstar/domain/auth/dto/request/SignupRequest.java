package com.common.solstar.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "이메일은 필수 입력값 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력값 입니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력값 입니다.")
    private String nickname;

    private LocalDate birthDate;

    private String phone;

    @NotBlank(message = "계좌번호는 필수 입력값 입니다.")
    private String accountNumber;

    private String bankCode;

    @NotBlank(message = "계좌 비밀번호는 필수 입력값 입니다.")
    private String accountPassword;

    private String userKey;

}
