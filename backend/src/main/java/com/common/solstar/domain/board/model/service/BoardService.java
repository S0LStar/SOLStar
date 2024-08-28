package com.common.solstar.domain.board.model.service;

import com.common.solstar.domain.board.dto.request.BoardCreateRequestDto;
import com.common.solstar.domain.board.dto.request.BoardUpdateRequestDto;
import com.common.solstar.domain.board.dto.response.BoardResponseDto;
import com.common.solstar.domain.board.entity.Board;
import com.common.solstar.domain.board.model.repository.BoardRepository;
import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.model.repository.FundingRepository;
import com.common.solstar.domain.user.entity.User;
import com.common.solstar.domain.user.model.repository.UserRepository;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final FundingRepository fundingRepository;
    private final UserRepository userRepository;

    public void createBoard(int fundingId, BoardCreateRequestDto boardDto) {

        // 현재 로그인한 유저가 해당 펀딩의 host 인지 확인
        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        // 로그인한 유저의 id 가져오기
        // 현재는 User 구현되어있지 않기 때문에 임시로 id = 1 인 유저 찾기
        User loginUser = userRepository.findById(1);

        if (funding.getHost() != loginUser || loginUser == null) {
            throw new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION);
        }

        Board createdBoard = Board.createBoard(funding, boardDto.getTitle(), boardDto.getContent(), boardDto.getContentImage());

        boardRepository.save(createdBoard);
    }

    public List<BoardResponseDto> getBoardList(int fundingId) {
        List<Board> boardEntities = boardRepository.findByFunding_Id(fundingId);

        List<BoardResponseDto> responseDtos = boardEntities.stream()
                .map(board -> BoardResponseDto.createResponseDto(board))
                .collect(Collectors.toList());

        Collections.reverse(responseDtos);
        return responseDtos;
    }

    @Transactional
    public void updateBoard(int boardId, BoardUpdateRequestDto boardDto) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_BOARD_EXCEPTION));

        // 현재 로그인한 유저가 해당 펀딩의 host 인지 확인

        board.updateBoardDetails(boardDto);
    }

    @Transactional
    public void deleteBoard(int boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_BOARD_EXCEPTION));

        // 현재 로그인한 유저가 해당 펀딩의 host 인지 확인

        board.deleteBoard();
        boardRepository.save(board);
    }
}
