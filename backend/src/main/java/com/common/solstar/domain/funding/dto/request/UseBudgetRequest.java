package com.common.solstar.domain.funding.dto.request;

import lombok.Getter;

@Getter
public class UseBudgetRequest {

    private int fundingId;
    private String storeAccount;
    private String storeSummary;
}
