package com.common.solstar.domain.agency.dto.request;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNameRequest {

    @NotBlank(message = "이름은 필수 입력값 입니다.")
    private String name;

}
