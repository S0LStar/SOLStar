package com.common.solstar.domain.funding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UseBudgetRequest {

    @NotNull
    private int fundingId;

    @NotNull
    private int balance;

    @NotNull
    private String storeAccount;

    @NotBlank
    private String storeSummary;
}
