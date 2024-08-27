package com.shinhan.solstar.domain.funding.dto.response;

import com.shinhan.solstar.domain.funding.entity.Funding;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Builder
    private FundingResponseDto(int id, String title, String artistName, int remainDays, String status, int goalAmount, int totalAmount) {
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.remainDays = remainDays;
        this.status = status;
        this.goalAmount = goalAmount;
        this.totalAmount = totalAmount;
    }

    public static FundingResponseDto createResponseDto(Funding funding) {
        String artistName;

        if (funding.getArtist().getName() != null) artistName = funding.getArtist().getName();
        else artistName = funding.getArtist().getGroup();

        LocalDate deadlineDate = funding.getDeadlineDate();

        long remainDays = ChronoUnit.DAYS.between(LocalDate.now(), deadlineDate);

        return FundingResponseDto.builder()
                .id(funding.getId())
                .title(funding.getTitle())
                .artistName(artistName)
                .remainDays((int) (remainDays - 1))
                .status(funding.getStatus().getMessage())
                .goalAmount(funding.getGoalAmount())
                .totalAmount(funding.getTotalAmount())
                .build();
    }

}
