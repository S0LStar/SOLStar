package com.common.solstar.domain.fundingJoin.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FundingJoinCreateRequestDto {

    @NotNull
    private int fundingId;

    @NotNull
    private int amount;

    @NotNull
    private String password;

}
