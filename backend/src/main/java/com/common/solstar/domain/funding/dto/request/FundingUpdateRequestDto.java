package com.common.solstar.domain.funding.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FundingUpdateRequestDto {

    private String title;

    private String fundingImage;

    private String content;

    private String contentImage;
}
