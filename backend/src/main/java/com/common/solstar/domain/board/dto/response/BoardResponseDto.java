package com.common.solstar.domain.board.dto.response;

import com.common.solstar.domain.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {

    private int id;

    private String title;

    private String content;

    private String contentImage;

    private LocalDateTime createDate;

    public static BoardResponseDto createResponseDto(Board board) {
        return new BoardResponseDto(board.getId(), board.getTitle(), board.getContent(), board.getContentImage(), board.getCreateDate());
    }
}
