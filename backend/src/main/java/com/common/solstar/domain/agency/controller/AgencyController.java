package com.common.solstar.domain.agency.controller;

import com.common.solstar.domain.agency.dto.request.UpdateNameRequest;
import com.common.solstar.domain.agency.dto.request.UpdateProfileImageRequest;
import com.common.solstar.domain.agency.dto.response.AgencyDetailResponse;
import com.common.solstar.domain.agency.model.service.AgencyService;
import com.common.solstar.domain.funding.dto.response.FundingAgencyResponseDto;
import com.common.solstar.global.auth.jwt.JwtUtil;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.common.solstar.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agency")
@RequiredArgsConstructor
@Tag(name = "AgencyController")
public class AgencyController {

    private final AgencyService agencyService;
    private final JwtUtil jwtUtil;

    // 펀딩 인증 요청 조회
    @GetMapping("/funding")
    @Operation(summary = "펀딩 인증 요청 조회")
    public ResponseEntity<?> getFundingList(@RequestHeader(value = "Authorization", required = false) String header) {
        String accessToken = header.substring(7);
        String authEmail = jwtUtil.getLoginUser(header);
        String role = jwtUtil.roleFromToken(accessToken);

        if (role.equals("USER"))
            throw new ExceptionResponse(CustomException.NO_AUTH_FUNDING_ACCEPT_EXCEPTION);

        List<FundingAgencyResponseDto> fundingList = agencyService.getFundingList(authEmail);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("펀딩 인증 요청 리스트 조회 성공")
                .data(fundingList)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 펀딩 인증 요청 승인
    @PatchMapping("/funding-accept/{fundingId}")
    @Operation(summary = "펀딩 인증 요청 승인")
    public ResponseEntity<?> acceptFunding(@PathVariable("fundingId") int fundingId,
                                           @RequestHeader(value = "Authorization", required = false) String header) {
        String authEmail = jwtUtil.getLoginUser(header);
        agencyService.acceptFunding(fundingId, authEmail);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("펀딩 인증 요청 승인 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 펀딩 인증 요청 거절
    @PatchMapping("/funding-reject/{fundingId}")
    @Operation(summary = "펀딩 인증 요청 거절")
    public ResponseEntity<?> rejectFunding(@PathVariable("fundingId") int fundinId,
                                           @RequestHeader(value = "Authorization", required = false) String header) {
        String authEmail = jwtUtil.getLoginUser(header);
        agencyService.rejectFunding(fundinId, authEmail);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("펀딩 인증 요청 거절 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "로그인한 소속사 계정 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<AgencyDetailResponse>> getAgencyDetail(@RequestHeader(value = "Authorization", required = false) String header) {
        String authEmail = jwtUtil.getLoginUser(header);
        AgencyDetailResponse result = agencyService.getAgencyDetail(authEmail);
        ResponseDto<AgencyDetailResponse> responseDto = ResponseDto.<AgencyDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("소속사 계정 조회 성공")
                .data(result)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @Operation(summary = "소속사 프로필 이미지 수정")
    @PostMapping("/profileImage")
    public ResponseEntity<ResponseDto<String>> updateProfileImage (@RequestHeader(value = "Authorization", required = false) String header,
                                                                   @RequestBody UpdateProfileImageRequest request){
        String authEmail = jwtUtil.getLoginUser(header);
        agencyService.updateProfileImage(authEmail, request);
        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("프로필 이미지 수정 성공")
                .data(null)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "소속사 이름 수정")
    @PostMapping("/name")
    public ResponseEntity<ResponseDto<String>> updateName(@RequestHeader(value = "Authorization", required = false) String header,
                                                          @Valid @RequestBody UpdateNameRequest request){
        String authEmail = jwtUtil.getLoginUser(header);
        agencyService.updateName(authEmail, request);
        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("이름 수정 성공")
                .data(null)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
