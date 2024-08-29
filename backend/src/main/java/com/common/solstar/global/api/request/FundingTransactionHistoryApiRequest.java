package com.common.solstar.global.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundingTransactionHistoryApiRequest {

    @JsonProperty("Header")
    private CommonHeader header;

    private String accountNo;
    private String startDate;
    private String endDate;
    private String transactionType;
    private String orderByType;

}
