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
    private String transactionDateTime;
    private String transactionTypeName;
    private int transactionBalance;
    private int transactionAfterBalance;
    private String transactionSummary;
}
