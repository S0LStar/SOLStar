package com.common.solstar.domain.funding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandDepositAccountResponse {

    private String bankCode;

    private String accountNo;
}
