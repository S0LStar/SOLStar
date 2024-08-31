package com.common.solstar.domain.user.controller;

import com.common.solstar.domain.agency.model.service.AgencyService;
import com.common.solstar.domain.funding.dto.response.FundingResponseDto;
import com.common.solstar.domain.user.dto.request.UpdateIntroductionRequest;
import com.common.solstar.domain.user.dto.request.UpdateNicknameRequest;
import com.common.solstar.domain.user.dto.request.UpdateProfileImageRequest;
import com.common.solstar.domain.user.dto.response.HostFundingResponse;
import com.common.solstar.domain.user.dto.response.UserDetailResponse;
import com.common.solstar.domain.user.dto.response.UserJoinFundingReponse;
import com.common.solstar.domain.user.model.service.UserService;
import com.common.solstar.global.auth.jwt.JwtUtil;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.common.solstar.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "UserController", description = "회원 관련 API")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AgencyService agencyService;

    @Operation(summary = "로그인 유저 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<UserDetailResponse>> getLoginUserDetail(@RequestHeader(value = "Authorization", required = false) String header) {
        String authEmail = jwtUtil.getLoginUser(header);
        System.out.println(authEmail);
        UserDetailResponse result = userService.getLoginUserDetail(authEmail);
        ResponseDto<UserDetailResponse> responseDto = ResponseDto.<UserDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("로그인 유저 정보 조회 성공")
                .data(result)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "나의 참여 펀딩 조회")
    @GetMapping("/join-funding")
    public ResponseEntity<ResponseDto<List<FundingResponseDto>>> getUserJoinFunding(@RequestHeader(value = "Authorization", required = false) String header) {
        String authEmail = jwtUtil.getLoginUser(header);
        List<FundingResponseDto> result = userService.getUserJoinFunding(authEmail);
        ResponseDto<List<FundingResponseDto>> responseDto = ResponseDto.<List<FundingResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("나의 참여 펀딩 조회 성공")
                .data(result)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "나의 주최 펀딩 조회")
    @GetMapping("/host-funding")
    public ResponseEntity<ResponseDto<List<FundingResponseDto>>> getHostFunding(@RequestHeader(value = "Authorization", required = false) String header){
        String authEmail = jwtUtil.getLoginUser(header);
        List<FundingResponseDto> result = userService.getHostFunding(authEmail);
        ResponseDto<List<FundingResponseDto>> responseDto = ResponseDto.<List<FundingResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("나의 주최 펀딩 조회 성공")
                .data(result)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "특정 유저가 주최한 펀딩 조회")
    @GetMapping("/host-funding/{userId}")
    public ResponseEntity<ResponseDto<List<FundingResponseDto>>> getHostFundingByUserId(@RequestHeader(value = "Authorization", required = false) String header,
                                                                                        @PathVariable("userId") int userId){
        String authEmail = jwtUtil.getLoginUser(header);
        List<FundingResponseDto> result = userService.getHostFundingById(authEmail, userId);
        ResponseDto<List<FundingResponseDto>> responseDto = ResponseDto.<List<FundingResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("해당 유저의 주최 펀딩 조회 성공")
                .data(result)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "자기소개 수정")
    @PatchMapping("/introduction")
    public ResponseEntity<ResponseDto<String>> updateIntroduction(@RequestHeader(value = "Authorization", required = false) String header,
                                                                  @RequestBody UpdateIntroductionRequest request) {
        String authEmail = jwtUtil.getLoginUser(header);
        userService.updateIntroduction(authEmail, request);
        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("자기소개 수정 성공")
                .data(null)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<String>> logout(@RequestHeader(value = "Authorization", required = false) String header,
                                                      @CookieValue(value = "refreshToken", required = false)Cookie cookie,
                                                      HttpServletResponse response) {

        String accessToken = header.substring(7);
        String authEmail = jwtUtil.getLoginUser(header);
        String role = jwtUtil.roleFromToken(accessToken);

        // 토큰에 담긴 role이 USER면
        if(role.equals("USER")){
            userService.logout(authEmail);
        }
        else {
            agencyService.logout(authEmail);
        }

        // refresh token 쿠키 삭제
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.setHeader("Set-Cookie", deleteCookie.toString());

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("로그아웃 성공")
                .data(null)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "프로필 이미지 수정")
    @PostMapping("/profileImage")
    public ResponseEntity<ResponseDto<String>> updateProfileImage (@RequestHeader(value = "Authorization", required = false) String header,
                                                                   @RequestBody UpdateProfileImageRequest request){
        String authEmail = jwtUtil.getLoginUser(header);
        userService.updateProfileImage(authEmail, request);
        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("프로필 이미지 수정 성공")
                .data(null)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "닉네임 수정")
    @PostMapping("/nickname")
    public ResponseEntity<ResponseDto<String>> updateNickname (@RequestHeader(value = "Authorization", required = false) String header,
                                                               @Valid @RequestBody UpdateNicknameRequest request){
        String authEmail = jwtUtil.getLoginUser(header);
        userService.updateNickname(authEmail, request);
        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("닉네임 수정 성공")
                .data(null)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
