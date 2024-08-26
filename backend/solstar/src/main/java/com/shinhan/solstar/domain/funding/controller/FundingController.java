package com.shinhan.solstar.domain.funding.controller;

import com.shinhan.solstar.domain.funding.dto.request.FundingCreateRequestDto;
import com.shinhan.solstar.domain.funding.dto.request.FundingUpdateRequestDto;
import com.shinhan.solstar.domain.funding.model.service.FundingService;
import com.shinhan.solstar.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funding")
@RequiredArgsConstructor
public class FundingController {

    private final FundingService fundingService;

    // 펀딩 생성
    @PostMapping
    public ResponseEntity<?> createFunding(@RequestBody FundingCreateRequestDto fundingDto) {
        System.out.println("컨트롤러 들어옴");
        fundingService.createFunding(fundingDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("펀딩 생성 완료")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 펀딩 수정
    @PatchMapping("/{fundingId}")
    public ResponseEntity<?> updateFunding(@PathVariable("fundingId") int fundingId, @RequestBody FundingUpdateRequestDto fundingDto) {
        fundingService.updateFunding(fundingId, fundingDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("펀딩 수정 완료")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 펀딩 삭제

    // 펀딩 상세 정보 조회

    // 펀딩 프로젝트 소개 조회

    // 펀딩 공지 작성하기

    // 펀딩 공지사항 조회

    // 펀딩 공지사항 수정

    // 펀딩 공지사항 삭제

    // 펀딩 정산 조회

    // 펀딩 정산 작성

    // 펀딩 정산 - 결제내역 불러오기

    // 펀딩 참여하기

    // 쏠 스코어 남기기

    // 메인화면 - 나의 선호 아티스트 펀딩 조회

    // 메인화면 - 최근 인기 펀딩 조회

    // 메인화면 - 검색어로 펀딩 조회

}
