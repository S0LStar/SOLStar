package com.shinhan.solstar.domain.funding.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FundingType {
    COMMON("일반 펀딩"), VERIFIED("인증 펀딩"), ADVERTISE("홍보");

    private String message;

    public static FundingType fromString(String type) {
        try {
            return FundingType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("올바르지 않은 형태의 펀딩 타입입니다.");
        }
    }
}
