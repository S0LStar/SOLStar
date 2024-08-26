package com.shinhan.solstar.domain.board.model.service;

import com.shinhan.solstar.domain.board.dto.request.BoardCreateRequestDto;
import com.shinhan.solstar.domain.board.entity.Board;
import com.shinhan.solstar.domain.board.model.repository.BoardRepository;
import com.shinhan.solstar.domain.funding.entity.Funding;
import com.shinhan.solstar.domain.funding.model.repository.FundingRepository;
import com.shinhan.solstar.domain.user.entity.User;
import com.shinhan.solstar.domain.user.model.repository.UserRepository;
import com.shinhan.solstar.global.exception.CustomException;
import com.shinhan.solstar.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        Board createdBoard = Board.createBoard(funding, boardDto.getTitle(), boardDto.getContent());

        boardRepository.save(createdBoard);
    }
}
