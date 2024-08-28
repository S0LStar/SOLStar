package com.common.solstar.domain.agency.controller;

import com.common.solstar.domain.agency.model.service.AgencyService;
import com.common.solstar.domain.funding.dto.response.FundingAgencyResponseDto;
import com.common.solstar.global.auth.jwt.JwtUtil;
import com.common.solstar.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        String authEmail = jwtUtil.getLoginUser(header);
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
    public ResponseEntity<?> acceptFunding(@PathVariable("fundingId") int fundingId) {
        agencyService.acceptFunding(fundingId);

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
    public ResponseEntity<?> rejectFunding(@PathVariable("fundingId") int fundinId) {
        agencyService.rejectFunding(fundinId);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("펀딩 인증 요청 거절 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
