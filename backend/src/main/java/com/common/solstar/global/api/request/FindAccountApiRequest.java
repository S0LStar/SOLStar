package com.common.solstar.global.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class FindAccountApiRequest {

    @JsonProperty("Header")
    private CommonHeader header;

    private String accountNo;
}
