package com.shinhan.solstar.domain.funding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FundingCreateRequestDto {

    @NotNull
    private String type;

    private String fundingImage;

    @NotNull
    private String title;

    private LocalDateTime deadlineDate;

    private int goalAmount;

    @NotNull
    private int artistId;

    @NotNull
    private String content;

    private String contentImage;

}
