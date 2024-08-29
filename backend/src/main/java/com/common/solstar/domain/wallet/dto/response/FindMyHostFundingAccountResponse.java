package com.common.solstar.domain.wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindMyHostFundingAccountResponse {

    private int fundingId;
    private String accountNo;
    private int accountBalance;
    private String artistName;
    private String userName;

}
