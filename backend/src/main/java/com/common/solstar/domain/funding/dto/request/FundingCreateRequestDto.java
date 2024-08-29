package com.common.solstar.domain.funding.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FundingCreateRequestDto {

    @NotNull
    private String type;

    private String fundingImage;

    @NotNull
    private String title;

    private LocalDate deadlineDate;

    private int goalAmount;

    @NotNull
    private int artistId;

    @NotNull
    private String content;

}
