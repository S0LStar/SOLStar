package com.common.solstar.domain.auth.controller;

import com.common.solstar.domain.auth.dto.request.LoginRequest;
import com.common.solstar.domain.auth.dto.request.SignupRequest;
import com.common.solstar.domain.auth.dto.request.UserAccountValidateRequest;
import com.common.solstar.domain.auth.dto.response.LoginResponse;
import com.common.solstar.domain.auth.dto.response.RefreshResponse;
import com.common.solstar.domain.auth.dto.response.UserAccountValidateResponse;
import com.common.solstar.domain.auth.model.service.AuthService;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.common.solstar.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "AuthContoller", description = "토큰 없이 전급 가능한 요청")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<String>> signup (@RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("회원가입 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponse>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse result = authService.login(loginRequest);

        // refresh token은 쿠키에 저장하여 응답 보내줌
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", result.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7*24*60*60)
                .sameSite("Strict")
                .build();

        response.setHeader("Set-Cookie", refreshCookie.toString());

        ResponseDto<LoginResponse> responseDto = ResponseDto.<LoginResponse>builder()
                .status(HttpStatus.OK.value())
                .message("로그인 성공")
                .data(result)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<ResponseDto<RefreshResponse>> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if(refreshToken == null) {
            throw new ExceptionResponse(CustomException.EXPIRED_JWT_EXCEPTION);
        }
        RefreshResponse result = authService.refresh(refreshToken);

        ResponseDto<RefreshResponse> responseDto = ResponseDto.<RefreshResponse>builder()
                .status(HttpStatus.OK.value())
                .message("새 액세스 토큰 발급 성공")
                .data(result)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "사용자 계정 조회")
    @PostMapping("/user-account/validate")
    public ResponseEntity<ResponseDto<UserAccountValidateResponse>> userAccountValidate (@RequestBody UserAccountValidateRequest request) {
        UserAccountValidateResponse result = authService.validateUser(request);

        ResponseDto<UserAccountValidateResponse> responseDto = ResponseDto.<UserAccountValidateResponse>builder()
                .status(HttpStatus.OK.value())
                .message("금융 API 사용자 계정 조회 성공")
                .data(result)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
