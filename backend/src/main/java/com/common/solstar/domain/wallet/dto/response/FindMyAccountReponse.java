package com.common.solstar.domain.wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindMyAccountReponse {

    private String accountNo;
    private String userName;
    private int accountBalance;

}
