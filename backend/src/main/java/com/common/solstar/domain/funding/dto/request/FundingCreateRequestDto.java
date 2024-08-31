package com.common.solstar.domain.funding.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FundingCreateRequestDto {

    @NotNull
    private String type;

    private MultipartFile fundingImage;

    @NotNull
    private String title;

    private LocalDate deadlineDate;

    private int goalAmount;

    @NotNull
    private int artistId;

    @NotNull
    private String content;

}
