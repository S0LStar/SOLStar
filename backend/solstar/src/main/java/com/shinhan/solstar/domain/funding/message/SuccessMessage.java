package com.shinhan.solstar.domain.funding.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessMessage {

    SUCCESS_CREATE_FUNDING("펀딩 생성 완료"),
    SUCCESS_UPDATE_FUNDING("펀딩 수정 완료"),
    SUCCESS_DELETE_FUNDING("펀딩 삭제 완료")
    ;

    private String message;
}
