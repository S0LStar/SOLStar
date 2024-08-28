package com.common.solstar.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostFundingResponse {

    private int id;
    private String artist;
    private String title;
    private String status;
    private int totalAmount;
    private int goalAmount;
    private String type;
    private String fundingImage;

}
