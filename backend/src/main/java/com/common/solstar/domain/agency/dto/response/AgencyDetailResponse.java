package com.common.solstar.domain.agency.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgencyDetailResponse {

    private String profileImage;
    private String name;
    private String email;
    private String phone;

}
