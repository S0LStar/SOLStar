package com.shinhan.solstar.domain.auth.dto.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshResponse {

    String accessToken;

}
