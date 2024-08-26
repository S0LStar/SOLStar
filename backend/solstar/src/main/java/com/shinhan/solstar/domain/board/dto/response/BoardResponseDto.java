package com.shinhan.solstar.domain.board.dto.response;

import com.shinhan.solstar.domain.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {

    private String title;

    private String content;

    private LocalDateTime createDate;

    public static BoardResponseDto createResponseDto(Board board) {
        return new BoardResponseDto(board.getTitle(), board.getContent(), board.getCreateDate());
    }
}
