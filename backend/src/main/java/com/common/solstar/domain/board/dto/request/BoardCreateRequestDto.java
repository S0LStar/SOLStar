package com.common.solstar.domain.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateRequestDto {

    @NotNull
    private String title;

    @NotNull
    private String content;

}
