package com.common.solstar.domain.user.controller;

import com.common.solstar.domain.user.dto.request.UpdateIntroductionRequest;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "로그인 유저 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<UserDetailResponse>> getLoginUserDetail(@RequestHeader(value = "Authorization", required = false) String header) {
        String authEmail = jwtUtil.getEmailFromToken(header);
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
    public ResponseEntity<ResponseDto<List<UserJoinFundingReponse>>> getUserJoinFunding(@RequestHeader(value = "Authorization", required = false) String header) {
        String authEmail = jwtUtil.getEmailFromToken(header);
        List<UserJoinFundingReponse> result = userService.getUserJoinFunding(authEmail);
        ResponseDto<List<UserJoinFundingReponse>> responseDto = ResponseDto.<List<UserJoinFundingReponse>>builder()
                .status(HttpStatus.OK.value())
                .message("나의 참여 펀딩 조회 성공")
                .data(result)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "나의 주최 펀딩 조회")
    @GetMapping("/host-funding")
    public ResponseEntity<ResponseDto<List<HostFundingResponse>>> getHostFunding(@RequestHeader(value = "Authorization", required = false) String header){
        String authEmail = jwtUtil.getEmailFromToken(header);
        List<HostFundingResponse> result = userService.getHostFunding(authEmail);
        ResponseDto<List<HostFundingResponse>> responseDto = ResponseDto.<List<HostFundingResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("나의 주최 펀딩 조회 성공")
                .data(result)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "자기소개 수정")
    @PatchMapping("/introduction")
    public ResponseEntity<ResponseDto<String>> updateIntroduction(@RequestHeader(value = "Authorization", required = false) String header,
                                                                  @RequestBody UpdateIntroductionRequest request) {
        String authEmail = jwtUtil.getEmailFromToken(header);
        userService.updateIntroduction(authEmail, request);
        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("자기소개 수정 성공")
                .data(null)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
