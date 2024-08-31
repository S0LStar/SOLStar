package com.common.solstar.domain.funding.dto.request;

import com.common.solstar.domain.funding.entity.Funding;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferTotalAmountRequest {

    private String accountNo;
    private int totalAmount;
}
