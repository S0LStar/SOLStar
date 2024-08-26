package com.shinhan.solstar.domain.funding.model.service;

import com.shinhan.solstar.domain.funding.dto.request.FundingCreateRequestDto;
import com.shinhan.solstar.domain.funding.dto.request.FundingUpdateRequestDto;

public interface FundingService {

    // 펀딩 생성
    void createFunding(FundingCreateRequestDto fundingDto);

    // 펀딩 수정
    void updateFunding(int fundingId, FundingUpdateRequestDto fundingDto);

    // 펀딩 삭제
    void deleteFunding(int fundingId);
}
