package com.common.solstar.domain.funding.dto.request;

import com.common.solstar.domain.fundingJoin.entity.FundingJoin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRefundRequest {

    private FundingJoin fundingJoin;
}
