package com.common.solstar.domain.funding.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DonateRequest {

    private String userKey;
    private String accountNo;
    private String restAmount;
}
