package com.common.solstar.domain.funding.controller;

import com.common.solstar.domain.board.dto.request.BoardCreateRequestDto;
import com.common.solstar.domain.board.dto.request.BoardUpdateRequestDto;
import com.common.solstar.domain.board.dto.response.BoardResponseDto;
import com.common.solstar.domain.board.model.service.BoardService;
import com.common.solstar.domain.funding.dto.request.FundingCreateRequestDto;
import com.common.solstar.domain.funding.dto.request.FundingUpdateRequestDto;
import com.common.solstar.domain.funding.dto.response.FundingContentResponseDto;
import com.common.solstar.domain.funding.dto.response.FundingDetailResponseDto;
import com.common.solstar.domain.funding.dto.response.FundingResponseDto;
import com.common.solstar.domain.funding.model.service.FundingService;
import com.common.solstar.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funding")
@RequiredArgsConstructor
@Tag(name = "FundingController")
public class FundingController {

    private final FundingService fundingService;
    private final BoardService boardService;

    // 펀딩 생성
    @PostMapping
    @Operation(summary = "펀딩 생성")
    public ResponseEntity<?> createFunding(@RequestBody FundingCreateRequestDto fundingDto) {
        System.out.println("컨트롤러 들어옴");
        fundingService.createFunding(fundingDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("펀딩 생성 완료")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 펀딩 수정
    @PatchMapping("/{fundingId}")
    @Operation(summary = "펀딩 수정")
    public ResponseEntity<?> updateFunding(@PathVariable("fundingId") int fundingId, @RequestBody FundingUpdateRequestDto fundingDto) {
        fundingService.updateFunding(fundingId, fundingDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("펀딩 수정 완료")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 펀딩 삭제
    @DeleteMapping("/{fundingId}")
    @Operation(summary = "펀딩 삭제")
    public ResponseEntity<?> deleteFunding(@PathVariable("fundingId") int fundingId) {
        fundingService.deleteFunding(fundingId);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("펀딩 삭제 완료")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 펀딩 상세 정보 조회
    @GetMapping("/{fundingId}")
    @Operation(summary = "펀딩 상세 정보 조회")
    public ResponseEntity<?> getFundingInfo(@PathVariable("fundingId") int fundingId) {
        FundingDetailResponseDto selectedFunding = fundingService.getFundingById(fundingId);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("펀딩 상세 조회 완료")
                .data(selectedFunding)
                .build();
        
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 펀딩 내용 조회
    @GetMapping("/content/{fundingId}")
    @Operation(summary = "펀딩 내용 조회")
    public ResponseEntity<?> getFundingContent(@PathVariable("fundingId") int fundingId) {
        FundingContentResponseDto selectedFunding = fundingService.getFundingContent(fundingId);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("펀딩 내용 조회 완료")
                .data(selectedFunding)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 펀딩 공지 작성하기
    @PostMapping("/notice/{fundingId}")
    @Operation(summary = "펀딩 공지 작성")
    public ResponseEntity<?> createBoard(@PathVariable("fundingId") int fundingId, @RequestBody BoardCreateRequestDto boardDto) {
        boardService.createBoard(fundingId, boardDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("펀딩 공지사항 생성 완료")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 펀딩 공지사항 조회
    @GetMapping("/notice/{fundingId}")
    @Operation(summary = "펀딩 공지사항 조회")
    public ResponseEntity<?> getBoardList(@PathVariable("fundingId") int fundingId) {
        List<BoardResponseDto> boardList = boardService.getBoardList(fundingId);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("펀딩 공지사항 조회 완료")
                .data(boardList)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 펀딩 공지사항 수정
    @PatchMapping("/notice/{boardId}")
    @Operation(summary = "펀딩 공지사항 수정")
    public ResponseEntity<?> updateBoard(@PathVariable("boardId") int boardId, @RequestBody BoardUpdateRequestDto boardDto) {
        boardService.updateBoard(boardId, boardDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("펀딩 공지사항 수정 완료")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 펀딩 공지사항 삭제
    @DeleteMapping("/notice/{boardId}")
    @Operation(summary = "펀딩 공지사항 삭제")
    public ResponseEntity<?> deleteBoard(@PathVariable("boardId") int boardId) {
        boardService.deleteBoard(boardId);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("펀딩 공지사항 삭제 완료")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 펀딩 정산 - 결제내역 불러오기

    // 펀딩 참여하기

    // 쏠 스코어 남기기

    // 메인화면 - 나의 선호 아티스트 펀딩 조회
    @GetMapping("/my-like-artist")
    @Operation(summary = "메인화면 - 나의 선호 아티스트 펀딩 조회")
    public ResponseEntity<?> getMyLikeArtistFunding() {
        List<FundingResponseDto> fundingList = fundingService.getMyLikeArtistFunding();

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("나의 선호 아티스트 펀딩 조회 완료")
                .data(fundingList)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 메인화면 - 최근 인기 펀딩 조회
    @GetMapping("/popular")
    @Operation(summary = "메인화면 - 최근 인기 펀딩 조회")
    public ResponseEntity<?> getPopularFunding() {
        List<FundingResponseDto> fundingList = fundingService.getPopularFunding();

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("최근 인기 펀딩 조회 완료")
                .data(fundingList)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 메인화면 - 검색어로 펀딩 조회
    @GetMapping
    @Operation(summary = "메인화면 - 검색 펀딩 조회")
    public ResponseEntity<?> getSearchFunding(@RequestParam String keyword) {
        List<FundingResponseDto> searchFundings = fundingService.getSearchFunding(keyword);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("펀딩 검색 조회 완료")
                .data(searchFundings)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
