package com.common.solstar.domain.funding.dto.response;

import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.entity.FundingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
public class FundingResponseDto {

    private int id;

    private String title;

    private String artistName;

    private int remainDays;

    private String status;

    private int goalAmount;

    private int totalAmount;

    private String fundingImage;

    @Builder
    private FundingResponseDto(int id, String title, String artistName, int remainDays, String status, int goalAmount, int totalAmount, String fundingImage) {
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.remainDays = remainDays;
        this.status = status;
        this.goalAmount = goalAmount;
        this.totalAmount = totalAmount;
        this.fundingImage = fundingImage;
    }

    public static FundingResponseDto createResponseDto(Funding funding) {
        String artistName;

        if (funding.getArtist().getName() != null) artistName = funding.getArtist().getName();
        else artistName = funding.getArtist().getGroup();

        LocalDate deadlineDate = funding.getDeadlineDate();

        long remainDays = ChronoUnit.DAYS.between(LocalDate.now(), deadlineDate);

        FundingStatus status = funding.getStatus();
        if (status == FundingStatus.CLOSED) status = FundingStatus.SUCCESS;

        return FundingResponseDto.builder()
                .id(funding.getId())
                .title(funding.getTitle())
                .artistName(artistName)
                .remainDays((int) (remainDays - 1))
                .status(status.name())
                .goalAmount(funding.getGoalAmount())
                .totalAmount(funding.getTotalAmount())
                .fundingImage(funding.getFundingImage())
                .build();
    }

}
