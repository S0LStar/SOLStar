package com.shinhan.solstar.domain.funding.dto.response;

import com.shinhan.solstar.domain.funding.entity.Funding;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FundingContentResponseDto {

    private String content;

    public static FundingContentResponseDto createResponseDto(Funding funding) {
        return new FundingContentResponseDto(funding.getContent());
    }
}
