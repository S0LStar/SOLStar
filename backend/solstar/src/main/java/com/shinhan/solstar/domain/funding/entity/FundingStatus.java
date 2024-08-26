package com.shinhan.solstar.domain.funding.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FundingStatus {
    PROCESSING("진행중인 펀딩"), SUCCESS("펀딩 성공"), FAIL("펀딩 실패"), CLOSED("종료된 펀딩");

    private String message;
}
