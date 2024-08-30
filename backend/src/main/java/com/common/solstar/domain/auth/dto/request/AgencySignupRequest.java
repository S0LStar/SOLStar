package com.common.solstar.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgencySignupRequest {

    private String email;
    private String password;
    private String name;
    private String phone;
    private String profileImage;

}
