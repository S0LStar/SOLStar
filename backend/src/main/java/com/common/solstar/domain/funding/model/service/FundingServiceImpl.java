package com.common.solstar.domain.funding.model.service;

import com.common.solstar.domain.account.entity.Account;
import com.common.solstar.domain.account.model.repository.AccountRepository;
import com.common.solstar.domain.artist.entity.Artist;
import com.common.solstar.domain.artist.model.repository.ArtistRepository;
import com.common.solstar.domain.funding.dto.request.FundingCreateRequestDto;
import com.common.solstar.domain.funding.dto.request.FundingUpdateRequestDto;
import com.common.solstar.domain.funding.dto.request.InquireAccountRequestDto;
import com.common.solstar.domain.funding.dto.request.TransferJoinRequest;
import com.common.solstar.domain.funding.dto.response.*;
import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.entity.FundingStatus;
import com.common.solstar.domain.funding.entity.FundingType;
import com.common.solstar.domain.funding.model.repository.FundingRepository;
import com.common.solstar.domain.fundingAgency.entity.FundingAgency;
import com.common.solstar.domain.fundingAgency.model.repository.FundingAgencyRepository;
import com.common.solstar.domain.fundingJoin.dto.request.FundingJoinCreateRequestDto;
import com.common.solstar.domain.fundingJoin.entity.FundingJoin;
import com.common.solstar.domain.fundingJoin.model.repository.FundingJoinRepository;
import com.common.solstar.domain.likeList.entity.LikeList;
import com.common.solstar.domain.likeList.model.repository.LikeListRepository;
import com.common.solstar.domain.user.entity.User;
import com.common.solstar.domain.user.model.repository.UserRepository;
import com.common.solstar.global.api.request.CommonHeader;
import com.common.solstar.domain.funding.dto.request.DonateRequest;
import com.common.solstar.global.api.request.FindAccountApiRequest;
import com.common.solstar.global.api.request.TransferApiRequest;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FundingServiceImpl implements FundingService {

    private final FundingRepository fundingRepository;
    private final FundingAgencyRepository fundingAgencyRepository;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final LikeListRepository likeListRepository;
    private final FundingJoinRepository fundingJoinRepository;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://finopenapi.ssafy.io/ssafy/api/v1")
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AccountRepository accountRepository;

    @Value("${ssafy.api.key}")
    private String apiKey;

    @Value("${system.account.no}")
    private String systemAccountNo;

    @Override
    public void createFunding(FundingCreateRequestDto fundingDto, String authEmail) {

        Artist artist = artistRepository.findById(fundingDto.getArtistId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_ARTIST_EXCEPTION));

        // User 생성하여 host 찾기
        User host = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        // String 타입의 입력값을 FundingType으로 변환
        FundingType fundingType = FundingType.fromString(fundingDto.getType());

        Funding createdFunding = Funding.createFunding(fundingDto.getTitle(), fundingDto.getFundingImage(), fundingDto.getContent(), fundingDto.getGoalAmount(),
                fundingDto.getDeadlineDate(), 0, 0, artist, host, fundingType, FundingStatus.PROCESSING);

        fundingRepository.save(createdFunding);

        if (createdFunding.getType().equals(FundingType.VERIFIED)) {
            FundingAgency fundingAgency = FundingAgency.createFundingAgency(createdFunding, createdFunding.getArtist().getAgency());

            fundingAgencyRepository.save(fundingAgency);
        }
    }

    @Override
    @Transactional
    public void updateFunding(int fundingId, FundingUpdateRequestDto fundingDto, String authEmail) {

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        User host = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        if (funding.getHost().equals(host))
            throw new ExceptionResponse(CustomException.INVALID_FUNDING_HOST_EXCEPTION);

        funding.updateFundingDetails(fundingDto);
    }

    @Override
    @Transactional
    public void deleteFunding(int fundingId, String authEmail) {

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        User host = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        if (funding.getHost().equals(host))
            throw new ExceptionResponse(CustomException.INVALID_FUNDING_HOST_EXCEPTION);

        funding.deleteFunding();
        fundingRepository.save(funding);
    }

    @Override
    public FundingDetailResponseDto getFundingById(int fundingId, String authEmail) {

        // 로그인한 유저 찾기
        User loginUser = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        FundingDetailResponseDto responseDto = FundingDetailResponseDto.createResponseDto(funding);

        if (funding.getHost().equals(loginUser)) {
            // 로그인한 유저가 주최자인 경우
            responseDto.setJoinStatus(2);
        } else if (fundingJoinRepository.existsByUserAndFunding(loginUser, funding)) {
            // 로그인한 유저가 참여자인 경우
            responseDto.setJoinStatus(1);
        }

        return responseDto;
    }

    @Override
    public FundingContentResponseDto getFundingContent(int fundingId, String authEmail) {

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        return FundingContentResponseDto.createResponseDto(funding);
    }

    @Override
    @Transactional
    public void joinFunding(FundingJoinCreateRequestDto joinFundingDto, String authEmail) {

        // 로그인한 유저 찾기
        User loginUser = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        // 해당 유저가 소유한 계좌가 있는지 확인
        Account account = loginUser.getAccount();

        // 비밀번호가 맞는지 확인
        if (!joinFundingDto.getPassword().equals(account.getPassword())) {
            throw new ExceptionResponse(CustomException.NOT_MATCH_ACCOUNT_PASSWORD_EXCEPTION);
        }

        // 펀딩에 대한 유효성 검사
        // 펀딩 존재 유무
        Funding funding = fundingRepository.findById(joinFundingDto.getFundingId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        // 펀딩이 진행중인 펀딩인지 확인
        if (!funding.getStatus().equals(FundingStatus.PROCESSING))
            throw new ExceptionResponse(CustomException.NOT_PROCESSING_FUNDING_EXCEPTION);

        // FundingJoin 객체 만들어서 저장
        FundingJoin fundingJoin = new FundingJoin(joinFundingDto.getFundingId(), loginUser, funding, joinFundingDto.getAmount(), LocalDateTime.now());

        // 펀딩 참여 금액 이체를 위한 request 생성
        TransferJoinRequest joinRequest = TransferJoinRequest.builder()
                .userKey(loginUser.getUserKey())
                .fundingJoin(fundingJoin)
                .build();

        // 펀딩 계좌 이체 가능한지 확인
        if(!transferFunding(joinRequest).isSuccess())
            throw new ExceptionResponse(CustomException.INVALID_FUNDING_JOIN_EXCEPTION);

        fundingJoinRepository.save(fundingJoin);

        // totalAmount와 totalJoin 업데이트
        funding.updateByJoin(fundingJoin.getAmount());

        fundingRepository.save(funding);
    }

    @Override
    @Transactional
    public void doneFunding(int fundingId, String authEmail) {
        // 로그인한 유저가 funding의 host인지 확인
        User loginUser = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        if (!loginUser.equals(funding.getHost()))
            throw new ExceptionResponse(CustomException.INVALID_FUNDING_HOST_EXCEPTION);

        // 펀딩이 완료된 상태인지 확인
        if (!funding.getStatus().equals(FundingStatus.SUCCESS))
            throw new ExceptionResponse(CustomException.NOT_SUCCESS_FUNDING_EXCEPTION);

        // 펀딩 계좌에서 총 금액 찾아서 만약 남아있다면 "기부" 메시지로 이체 후 계좌 연결관계 제거
        // 만약 남아있지 않다면 바로 계좌 연결관계 제거
        Account fundingAccount = accountRepository.findByAccountNumber(funding.getAccount())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_ACCOUNT_EXCEPTION));

        InquireAccountRequestDto inquireRequestDto = InquireAccountRequestDto.builder()
                .userKey(loginUser.getUserKey())
                .accountNo(fundingAccount.getAccountNumber())
                .build();

        String restAmount = getRestAmount(inquireRequestDto);

        if (Integer.parseInt(restAmount) != 0) {
            DonateRequest donateRequest = DonateRequest.builder()
                    .accountNo(fundingAccount.getAccountNumber())
                    .restAmount(restAmount)
                    .build();

            donateRestAmount(donateRequest);
        }

        // 모든 기능 실행 후 펀딩 상태 CLOSED로 변경
        funding.setStatus(FundingStatus.CLOSED);
    }


    @Override
    public List<FundingResponseDto> getMyLikeArtistFunding(String authEmail) {

        // 로그인한 유저 찾기
        User loginUser = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));


        List<LikeList> likeListEntities = likeListRepository.findByUser_Id(loginUser.getId());

        // LikeList 엔티티에서 Artist 엔티티 추출
        List<Artist> likeArtistEntities = likeListEntities.stream()
                .map(LikeList::getArtist)
                .collect(Collectors.toList());

        List<Funding> fundingEntities = fundingRepository.findByArtistIn(likeArtistEntities);

        List<FundingResponseDto> fundingList = fundingEntities.stream()
                .map(funding -> FundingResponseDto.createResponseDto(funding))
                .collect(Collectors.toList());

        return fundingList;
    }

    @Override
    public List<FundingResponseDto> getPopularFunding(String authEmail) {

        // 로그인한 유저 찾기
        User loginUser = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        // totalJoin 기준으로 내림차순 정렬된 펀딩 목록을 가져옴
        List<Funding> popularFundings = fundingRepository.findPopularFundings();

        // Funding 엔티티를 FundingResponseDto 로 변환
        return popularFundings.stream()
                .map(FundingResponseDto::createResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FundingResponseDto> getSearchFunding(String keyword, String authEmail) {

        // 로그인한 유저 불러오기
        User loginUser = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        // 검색어로 펀딩 리스트 불러오기
        List<Funding> searchFundings = fundingRepository.findByTitleContainingIgnoreCase(keyword);

        return searchFundings.stream()
                .map(FundingResponseDto::createResponseDto)
                .collect(Collectors.toList());
    }
    
    // 펀딩 참여 시 시스템 계좌에 이체
    public TransferJoinResponse transferFunding(TransferJoinRequest request) {

        String url = "/edu/demandDeposit/updateDemandDepositAccountTransfer";

        CommonHeader header = CommonHeader.builder()
                .apiName("updateDemandDepositAccountTransfer")
                .apiServiceCode("updateDemandDepositAccountTransfer")
                .userKey(request.getUserKey())
                .apiKey(apiKey)
                .build();
        header.setCommonHeader();

        User participant = request.getFundingJoin().getUser();
        int amount = request.getFundingJoin().getAmount();

        // 시스템 계좌에 참여자의 펀딩 참여 금액 입금
        TransferApiRequest apiRequest = TransferApiRequest.builder()
                .header(header)
                .depositAccountNo(systemAccountNo)
                .depositTransactionSummary("펀딩 참여 입금 : " + participant.getName())
                .transactionBalance(Integer.toString(amount))
                .withdrawalAccountNo(participant.getAccount().getAccountNumber())
                .withdrawalTransactionSummary("펀딩 참여 출금")
                .build();

        try {

            // API 요청 보내기
            Mono<String> responseMono = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .bodyValue(apiRequest)
                    .retrieve()
                    .bodyToMono(String.class);

            String response = responseMono.block();

            JsonNode root = objectMapper.readTree(response);

            // 성공 상태면 true 반환
            if (root.has("REC")) {
                return TransferJoinResponse.builder()
                        .isSuccess(true)
                        .build();
            }

        } catch (WebClientResponseException e) {
          // WebClient 오류 응답 처리
          String errorBody = e.getResponseBodyAsString();

          try {
              // 오류 응답 JSON 파싱
              JsonNode root = objectMapper.readTree(errorBody);

              if (root.has("responseCode")) {
                  String responseCode = root.get("responseCode").asText();

                  // responseCode에 따른 커스텀 예외 처리
                  switch (responseCode) {
                      case "A1003":
                          throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
                      default:
                          throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
                  }
              } else {
                  throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
              }
          } catch (JsonProcessingException jsonParseException) {
              // JSON 파싱 오류 시 기본 예외 처리
              throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
          }
        } catch (Exception e) {
            throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
        }

        return TransferJoinResponse.builder()
                .isSuccess(false)
                .build();
    }

    // 펀딩 계좌 잔액 조회
    public String getRestAmount(InquireAccountRequestDto requestDto) {

        String url = "/edu/demandDeposit/inquireDemandDepositAccountBalance";

        CommonHeader commonHeader = CommonHeader.builder()
                .apiName("inquireDemandDepositAccountBalance")
                .apiServiceCode("inquireDemandDepositAccountBalance")
                .apiKey(apiKey)
                .userKey(requestDto.getUserKey())
                .build();
        commonHeader.setCommonHeader();

        FindAccountApiRequest apiRequest = FindAccountApiRequest.builder()
                .header(commonHeader)
                .accountNo(requestDto.getAccountNo())
                .build();

        try {

            // API 요청 보내기
            Mono<String> responseMono = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .bodyValue(apiRequest)
                    .retrieve()
                    .bodyToMono(String.class);

            String response = responseMono.block();
            JsonNode root = objectMapper.readTree(response);

            if (!root.has("REC"))
                throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);

            return root.get("REC").get("accountBalance").asText();
        } catch (WebClientResponseException | JsonProcessingException e) {
            throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
        }
    }

    // 펀딩 계좌 잔액 기부
    public void donateRestAmount(DonateRequest request) {
        String url = "/edu/demandDeposit/updateDemandDepositAccountTransfer";

        CommonHeader header = CommonHeader.builder()
                .apiName("updateDemandDepositAccountTransfer")
                .apiServiceCode("updateDemandDepositAccountTransfer")
                .userKey(request.getUserKey())
                .apiKey(apiKey)
                .build();
        header.setCommonHeader();

        TransferApiRequest apiRequest = TransferApiRequest.builder()
                .header(header)
                .depositAccountNo(systemAccountNo)
                .depositTransactionSummary("기부")
                .transactionBalance(request.getRestAmount())
                .withdrawalAccountNo(request.getAccountNo())
                .withdrawalTransactionSummary("기부")
                .build();

        try {
            // API 요청 보내기
            Mono<String> responseMono = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .bodyValue(apiRequest)
                    .retrieve()
                    .bodyToMono(String.class);

            String response = responseMono.block();
            JsonNode root = objectMapper.readTree(response);

            if (!root.has("REC"))
                throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);

        } catch (WebClientResponseException | JsonProcessingException e) {
            throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
        }
    }

}
