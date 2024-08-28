package com.common.solstar.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckAuthCodeRequest {

    private String accountNo;
    private String authCode;
    private String userKey;

}
