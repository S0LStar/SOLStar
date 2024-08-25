package com.shinhan.solstar.domain.funding.controller;

import com.shinhan.solstar.domain.funding.dto.FundingCreateRequestDto;
import com.shinhan.solstar.domain.funding.message.SuccessMessage;
import com.shinhan.solstar.domain.funding.model.service.FundingService;
import com.shinhan.solstar.global.util.HttpMethodCode;
import com.shinhan.solstar.global.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funding")
@RequiredArgsConstructor
public class FundingController {

    private final FundingService fundingService;
    private final HttpResponseUtil responseUtil;

    // 펀딩 생성
    @PostMapping
    public ResponseEntity<?> createFunding(@RequestBody FundingCreateRequestDto fundingDto) {
//        fundingService.createFunding(fundingDto);

        return responseUtil.createResponse(HttpMethodCode.POST, SuccessMessage.SUCCESS_CREATE_FUNDING.getMessage());
    }

    // 펀딩 수정

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
