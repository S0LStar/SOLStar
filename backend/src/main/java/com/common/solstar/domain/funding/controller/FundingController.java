package com.common.solstar.domain.funding.controller;

import com.common.solstar.domain.agency.model.service.AgencyService;
import com.common.solstar.domain.board.dto.request.BoardCreateRequestDto;
import com.common.solstar.domain.board.dto.request.BoardUpdateRequestDto;
import com.common.solstar.domain.board.dto.response.BoardResponseDto;
import com.common.solstar.domain.board.model.service.BoardService;
import com.common.solstar.domain.funding.dto.request.FundingCreateRequestDto;
import com.common.solstar.domain.funding.dto.request.FundingUpdateRequestDto;
import com.common.solstar.domain.funding.dto.request.UseBudgetRequest;
import com.common.solstar.domain.funding.dto.response.FundingContentResponseDto;
import com.common.solstar.domain.funding.dto.response.FundingDetailResponseDto;
import com.common.solstar.domain.funding.dto.response.FundingResponseDto;
import com.common.solstar.domain.funding.model.service.FundingService;
import com.common.solstar.domain.fundingJoin.dto.request.FundingJoinCreateRequestDto;
import com.common.solstar.domain.user.model.service.UserService;
import com.common.solstar.global.auth.jwt.JwtUtil;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.common.solstar.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/funding")
@RequiredArgsConstructor
@Tag(name = "FundingController")
public class FundingController {

    private final FundingService fundingService;
    private final BoardService boardService;
    private final UserService userService;
    private final AgencyService agencyService;
    private final JwtUtil jwtUtil;

    // 펀딩 생성
    @PostMapping
    @Operation(summary = "펀딩 생성")
    public ResponseEntity<?> createFunding(@RequestHeader(value = "Authorization", required = false) String header,
                                           @RequestBody FundingCreateRequestDto fundingDto) {
        String accessToken = header.substring(7);
        String authEmail = jwtUtil.getLoginUser(header);
        String role = jwtUtil.roleFromToken(accessToken);

        if (!role.equals("USER") && !role.equals("AGENCY"))
            throw new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION);

        fundingService.createFunding(fundingDto, authEmail);

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
    public ResponseEntity<?> updateFunding(@RequestHeader(value = "Authorization", required = false) String header,
                                           @PathVariable("fundingId") int fundingId, @RequestBody FundingUpdateRequestDto fundingDto) {
        String authEmail = jwtUtil.getLoginUser(header);
        fundingService.updateFunding(fundingId, fundingDto, authEmail);

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
    public ResponseEntity<?> deleteFunding(@RequestHeader(value = "Authorization", required = false) String header,
                                           @PathVariable("fundingId") int fundingId) {
        String authEmail = jwtUtil.getLoginUser(header);
        fundingService.deleteFunding(fundingId, authEmail);

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
    public ResponseEntity<?> getFundingInfo(@RequestHeader(value = "Authorization", required = false) String header,
                                            @PathVariable("fundingId") int fundingId) {
        String authEmail = jwtUtil.getLoginUser(header);
        FundingDetailResponseDto selectedFunding = fundingService.getFundingById(fundingId, authEmail);

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
    public ResponseEntity<?> getFundingContent(@RequestHeader(value = "Authorization", required = false) String header,
                                               @PathVariable("fundingId") int fundingId) {
        String authEmail = jwtUtil.getLoginUser(header);
        FundingContentResponseDto selectedFunding = fundingService.getFundingContent(fundingId, authEmail);

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
    public ResponseEntity<?> createBoard(@RequestHeader(value = "Authorization", required = false) String header,
                                         @PathVariable("fundingId") int fundingId,
                                         @RequestBody BoardCreateRequestDto boardDto) {
        String authEmail = jwtUtil.getLoginUser(header);
        boardService.createBoard(fundingId, boardDto, authEmail);

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
    public ResponseEntity<?> getBoardList(@RequestHeader(value = "Authorization", required = false) String header,
                                          @PathVariable("fundingId") int fundingId) {
        String authEmail = jwtUtil.getLoginUser(header);
        List<BoardResponseDto> boardList = boardService.getBoardList(fundingId, authEmail);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("펀딩 공지사항 조회 완료")
                .data(Map.of("noticeList", boardList))
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 펀딩 공지사항 수정
    @PatchMapping("/notice/{boardId}")
    @Operation(summary = "펀딩 공지사항 수정")
    public ResponseEntity<?> updateBoard(@RequestHeader(value = "Authorization", required = false) String header,
                                         @PathVariable("boardId") int boardId,
                                         @RequestBody BoardUpdateRequestDto boardDto) {
        String authEmail = jwtUtil.getLoginUser(header);
        boardService.updateBoard(boardId, boardDto, authEmail);

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
    public ResponseEntity<?> deleteBoard(@RequestHeader(value = "Authorization", required = false) String header,
                                         @PathVariable("boardId") int boardId) {
        String authEmail = jwtUtil.getLoginUser(header);
        boardService.deleteBoard(boardId, authEmail);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("펀딩 공지사항 삭제 완료")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 펀딩 참여하기
    @PostMapping("/join")
    @Operation(summary = "펀딩 참여")
    public ResponseEntity<?> joinFunding(@RequestHeader(value = "Authorization", required = false) String header,
                                         @RequestBody FundingJoinCreateRequestDto joinFundingDto) {
        String authEmail = jwtUtil.getLoginUser(header);
        fundingService.joinFunding(joinFundingDto, authEmail);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("펀딩 참여 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 펀딩 정산 완료
    @PatchMapping("/done/{fundingId}")
    @Operation(summary = "펀딩 정산 완료")
    public ResponseEntity<?> doneFunding(@RequestHeader(value = "Authorization", required = false) String header,
                                         @PathVariable("fundingId") int fundingId) {
        String authEmail = jwtUtil.getLoginUser(header);
        fundingService.doneFunding(fundingId, authEmail);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("펀딩 정산 완료 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 펀딩 정산 후 주최자가 사용
    @PostMapping("/use-money")
    @Operation(summary = "펀딩 정산금 이체 (주최자 사용)")
    public ResponseEntity<?> useMoney(@RequestHeader(value = "Authorization", required = false) String header,
                                      @RequestBody UseBudgetRequest useBudgetRequest) {
        String authEmail = jwtUtil.getLoginUser(header);
        fundingService.useMoney(authEmail, useBudgetRequest);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("펀딩 정산금 이체 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 메인화면 - 나의 선호 아티스트 펀딩 조회
    @GetMapping("/my-like-artist")
    @Operation(summary = "메인화면 - 나의 선호 아티스트 펀딩 조회")
    public ResponseEntity<?> getMyLikeArtistFunding(@RequestHeader(value = "Authorization", required = false) String header) {
        String authEmail = jwtUtil.getLoginUser(header);
        List<FundingResponseDto> fundingList = fundingService.getMyLikeArtistFunding(authEmail);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("나의 선호 아티스트 펀딩 조회 완료")
                .data(Map.of("fundingList", fundingList))
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 메인화면 - 최근 인기 펀딩 조회
    @GetMapping("/popular")
    @Operation(summary = "메인화면 - 최근 인기 펀딩 조회")
    public ResponseEntity<?> getPopularFunding(@RequestHeader(value = "Authorization", required = false) String header) {
        String authEmail = jwtUtil.getLoginUser(header);
        List<FundingResponseDto> fundingList = fundingService.getPopularFunding(authEmail);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("최근 인기 펀딩 조회 완료")
                .data(Map.of("fundingList", fundingList))
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 메인화면 - 검색어로 펀딩 조회
    @GetMapping
    @Operation(summary = "메인화면 - 검색 펀딩 조회")
    public ResponseEntity<?> getSearchFunding(@RequestHeader(value = "Authorization", required = false) String header,
                                              @RequestParam String keyword) {
        String authEmail = jwtUtil.getLoginUser(header);
        List<FundingResponseDto> searchFundings = fundingService.getSearchFunding(keyword, authEmail);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("펀딩 검색 조회 완료")
                .data(Map.of("fundingList", searchFundings))
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
