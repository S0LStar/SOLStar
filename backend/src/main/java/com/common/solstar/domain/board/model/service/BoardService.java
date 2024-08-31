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
import com.common.solstar.global.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final FundingRepository fundingRepository;
    private final UserRepository userRepository;
    private final ImageUtil imageUtil;

    @Transactional
    public void createBoard(int fundingId, BoardCreateRequestDto boardDto, String authEmail) {

        // 현재 로그인한 유저가 해당 펀딩의 host 인지 확인
        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        // 로그인한 유저의 id 가져오기
        User loginUser = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        if (funding.getHost() != loginUser || loginUser == null) {
            throw new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION);
        }

        Board createdBoard = Board.createBoard(funding, boardDto.getTitle(), boardDto.getContent());

        boardRepository.save(createdBoard);
    }

    public List<BoardResponseDto> getBoardList(int fundingId, String authEmail) {
        // 로그인한 유저 불러오기
        User loginUser = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        // 펀딩의 주최자인지 여부 판단
        boolean isHost = funding.getHost().equals(loginUser);

        List<Board> boardEntities = boardRepository.findByFunding_Id(fundingId);

        List<BoardResponseDto> responseDtos = boardEntities.stream()
                .map(board -> BoardResponseDto.createResponseDto(board, isHost))
                .collect(Collectors.toList());

        Collections.reverse(responseDtos);
        return responseDtos;
    }

    @Transactional
    public void updateBoard(int boardId, BoardUpdateRequestDto boardDto, String authEmail) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_BOARD_EXCEPTION));

        // 현재 로그인한 유저가 해당 펀딩의 host 인지 확인
        User loginUser = userRepository.findByEmail(authEmail)
                        .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        if (loginUser.equals(board.getFunding().getHost()))
            throw new ExceptionResponse(CustomException.INVALID_FUNDING_HOST_EXCEPTION);

        board.updateBoardDetails(boardDto);
    }

    @Transactional
    public void deleteBoard(int boardId, String authEmail) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_BOARD_EXCEPTION));

        // 현재 로그인한 유저가 해당 펀딩의 host 인지 확인
        User loginUser = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        if (loginUser.equals(board.getFunding().getHost()))
            throw new ExceptionResponse(CustomException.INVALID_FUNDING_HOST_EXCEPTION);

        board.deleteBoard();
        boardRepository.save(board);
    }
}
