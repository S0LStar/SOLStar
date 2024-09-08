package com.common.solstar.domain.auth.controller;

import com.common.solstar.domain.auth.dto.request.*;
import com.common.solstar.domain.auth.dto.response.*;
import com.common.solstar.domain.auth.model.service.AuthService;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.common.solstar.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "AuthContoller", description = "토큰 없이 전급 가능한 요청")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<String>> signup (@Valid @RequestBody SignupRequest signupRequest,
                                                       @RequestPart(required = false) MultipartFile profileImage) {
        authService.signup(signupRequest, profileImage);
        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("회원가입 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "소속사 회원가입")
    @PostMapping("/signup-agency")
    public ResponseEntity<ResponseDto<String>> signupAgency (@RequestBody AgencySignupRequest request) {
        authService.agencySignup(request);
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

    @Operation(summary = "1원 송금")
    @PostMapping("/account/authenticate")
    public ResponseEntity<ResponseDto<String>> openAccountAuth (@RequestBody OpenAccountAuthRequest request) {
        authService.sendAccountAuth(request);
        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("1원 인증 송금 성공")
                .data(null)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "1원 송금 검증")
    @PostMapping("/account/authenticate-check")
    public ResponseEntity<ResponseDto<CheckAuthCodeResponse>> checkAccountAuth (@RequestBody CheckAuthCodeRequest request){
        CheckAuthCodeResponse result = authService.checkAuthCode(request);
        ResponseDto<CheckAuthCodeResponse> responseDto = ResponseDto.<CheckAuthCodeResponse>builder()
                .status(HttpStatus.OK.value())
                .message("1원 송금 검증 결과입니다.")
                .data(result)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "이메일 중복 검사")
    @PostMapping("/email/duplicate-check")
    public ResponseEntity<ResponseDto<CheckDuplicateResponse>> checkEmailDuplicate (@RequestBody EmailCheckDuplicateRequest request){
        ResponseDto<CheckDuplicateResponse> responseDto = ResponseDto.<CheckDuplicateResponse>builder()
                .status(HttpStatus.OK.value())
                .message("이메일 중복 검사 성공")
                .data(authService.checkEmail(request.getEmail()))
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "닉네임 중복 검사")
    @PostMapping("/nickname/duplicate-check")
    public ResponseEntity<ResponseDto<CheckDuplicateResponse>> checkNicknameDuplicate (@RequestBody NicknameCheckDuplicateRequest request){
        ResponseDto<CheckDuplicateResponse> responseDto = ResponseDto.<CheckDuplicateResponse>builder()
                .status(HttpStatus.OK.value())
                .message("닉네임 중복 검사 성공")
                .data(authService.checkNickname(request.getNickname()))
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
