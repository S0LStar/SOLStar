package com.common.solstar.domain.wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundingTransactionHistoryResponse {
    private String transactionDate;
    private String transactionTime;
    private String transactionTypeName;
    private String transactionBalance;
    private String transactionAfterBalance;
    private String transactionSummary;
}
