package com.shinhan.solstar.domain.funding.model.service;

import com.shinhan.solstar.domain.funding.dto.FundingCreateRequestDto;

public interface FundingService {

    // 펀딩 생성
    void createFunding(FundingCreateRequestDto fundingDto);
}
