package com.common.solstar.domain.funding.dto.response;

import com.common.solstar.domain.funding.entity.Funding;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FundingAgencyResponseDto {

    private int fundingId;

    private String title;

    private String artistName;

    private String fundingImage;

    public static FundingAgencyResponseDto createResponseDto(Funding funding) {
        return new FundingAgencyResponseDto(funding.getId(), funding.getTitle(), funding.getArtist().getName(), funding.getFundingImage());
    }
}
