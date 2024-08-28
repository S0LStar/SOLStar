package com.common.solstar.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountValidateRequest {

    private String userId;

}
