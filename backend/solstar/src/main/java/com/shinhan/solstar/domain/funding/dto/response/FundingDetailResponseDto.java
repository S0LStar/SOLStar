package com.shinhan.solstar.domain.funding.dto.response;

import com.shinhan.solstar.domain.funding.entity.Funding;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FundingDetailResponseDto {

    private String title;

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
    
    // 추가적으로 아티스트 관련 정보도 넘겨줘야 함
    @Builder
    private FundingDetailResponseDto(String title, String fundingImage, String hostNickname, String hostIntroduction, String hostProfileImage, int totalAmount, int goalAmount, LocalDate deadlineDate, int totalJoin, String type, String status) {
        this.title = title;
        this.fundingImage = fundingImage;
        this.hostNickname = hostNickname;
        this.hostIntroduction = hostIntroduction;
        this.hostProfileImage = hostProfileImage;
        this.totalAmount = totalAmount;
        this.goalAmount = goalAmount;
        this.deadlineDate = deadlineDate;
        this.totalJoin = totalJoin;
        this.type = type;
        this.status = status;
    }

    public static FundingDetailResponseDto createResponseDto(Funding funding) {
        return FundingDetailResponseDto.builder()
                .title(funding.getTitle())
                .fundingImage(funding.getFundingImage())
                .hostNickname(funding.getHost().getNickname())
                .hostIntroduction(funding.getHost().getIntroduction())
                .hostProfileImage(funding.getHost().getProfileImage())
                .totalAmount(funding.getTotalAmount())
                .goalAmount(funding.getGoalAmount())
                .deadlineDate(funding.getDeadlineDate())
                .totalJoin(funding.getTotalJoin())
                .type(funding.getType().getMessage())
                .status(funding.getStatus().getMessage())
                .build();
    }

}
