package com.common.solstar.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {

    private int id;
    private String name;
    private String nickname;
    private String profileImage;
    private String introduction;

}
