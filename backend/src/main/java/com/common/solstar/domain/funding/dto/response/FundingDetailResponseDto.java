package com.common.solstar.domain.funding.dto.response;

import com.common.solstar.domain.funding.entity.Funding;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Builder
public class FundingDetailResponseDto {

    private String title;

    private int artistId;

    private String artistProfileImage;

    private String artistName;

    private String fundingImage;

    private String hostNickname;

    private String hostIntroduction;

    private String hostProfileImage;

    private int totalAmount;

    private int goalAmount;

    private LocalDate deadlineDate;

    private int totalJoin;

    private String type;

    private String status;

    @Setter
    private int joinStatus;

    public static FundingDetailResponseDto createResponseDto(Funding funding) {
        return FundingDetailResponseDto.builder()
                .title(funding.getTitle())
                .artistId(funding.getArtist().getId())
                .artistProfileImage(funding.getArtist().getProfileImage())
                .artistName(funding.getArtist().getName())
                .fundingImage(funding.getFundingImage())
                .hostNickname(funding.getHost().getNickname())
                .hostIntroduction(funding.getHost().getIntroduction())
                .hostProfileImage(funding.getHost().getProfileImage())
                .totalAmount(funding.getTotalAmount())
                .goalAmount(funding.getGoalAmount())
                .deadlineDate(funding.getDeadlineDate())
                .totalJoin(funding.getTotalJoin())
                .type(funding.getType().name())
                .status(funding.getStatus().name())
                .joinStatus(0)
                .build();
    }

}
