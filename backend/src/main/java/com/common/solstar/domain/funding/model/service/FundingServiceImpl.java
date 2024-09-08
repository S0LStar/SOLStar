package com.common.solstar.domain.funding.model.service;

import com.common.solstar.domain.account.entity.Account;
import com.common.solstar.domain.account.model.repository.AccountRepository;
import com.common.solstar.domain.agency.entity.Agency;
import com.common.solstar.domain.agency.model.repository.AgencyRepository;
import com.common.solstar.domain.artist.entity.Artist;
import com.common.solstar.domain.artist.model.repository.ArtistRepository;
import com.common.solstar.domain.funding.dto.request.*;
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
import com.common.solstar.global.api.exception.WebClientExceptionHandler;
import com.common.solstar.global.api.request.CommonHeader;
import com.common.solstar.global.api.request.FindAccountApiRequest;
import com.common.solstar.global.api.request.TransferApiRequest;
import com.common.solstar.global.auth.jwt.JwtUtil;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.common.solstar.global.util.ImageUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final ImageUtil imageUtil;
    private final JwtUtil jwtUtil;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://finopenapi.ssafy.io/ssafy/api/v1")
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AccountRepository accountRepository;
    private final WebClientExceptionHandler webClientExceptionHandler;
    private final AgencyRepository agencyRepository;

    @Value("${ssafy.api.key}")
    private String apiKey;

    @Value("${system.account.no}")
    private String systemAccountNo;

    @Value("${system.user.key}")
    private String systemUserKey;

    @Override
    @Transactional
    public void createFunding(FundingCreateRequestDto fundingDto, String authEmail, MultipartFile fundingImage) {

        Artist artist = artistRepository.findById(fundingDto.getArtistId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_ARTIST_EXCEPTION));

        // 소속사인 경우 펀딩 생성 불가
        if (agencyRepository.existsByEmail(authEmail))
            throw new ExceptionResponse(CustomException.INVALID_FUNDING_HOST_EXCEPTION);

        // User 생성하여 host 찾기
        User host = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        // String 타입의 입력값을 FundingType으로 변환
        FundingType fundingType = FundingType.fromString(fundingDto.getType());

        // fundingImage 입력된 값 없다면 exception 처리
        String fundingImageInput = null;
        if (fundingImage.isEmpty()) {
            fundingImageInput = "https://hackerton.s3.ap-northeast-2.amazonaws.com/twice.png";
        } else {
            // s3에 fundingImage 저장
            imageUtil.uploadImage(fundingImage);

            // 이미지 funding에 저장할 String 값으로 변환
            fundingImageInput = imageUtil.upload(fundingImage);
        }

        // 두번째에 fundingImage 들어가야 함
        Funding createdFunding = Funding.createFunding(fundingDto.getTitle(), fundingImageInput, fundingDto.getContent(), fundingDto.getGoalAmount(),
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
    public FundingDetailResponseDto getFundingById(String header, int fundingId) {

        String accessToken = header.substring(7);
        String authEmail = jwtUtil.getLoginUser(header);
        String role = jwtUtil.roleFromToken(accessToken);

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        // USER인 경우 - 인증 수락 안된 펀딩은 조회 불가
        if(role.equals("USER")){

            User loginUser = userRepository.findByEmail(authEmail)
                    .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

            if (role.equals("USER") && funding.getType().equals(FundingType.VERIFIED)) {
                FundingAgency fundingAgency = fundingAgencyRepository.findByFunding(funding)
                        .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_AGENCY_EXCEPTION));

                if (!fundingAgency.isStatus())
                    throw new ExceptionResponse(CustomException.NOT_ACCEPT_FUNDING_EXCEPTION);
            }

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

        // AGENCY인 경우 - 모든 펀딩 조회 가능
        else {
            Agency loginUser = agencyRepository.findByEmail(authEmail)
                    .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AGENCY_EXCEPTION));

            FundingDetailResponseDto responseDto = FundingDetailResponseDto.createResponseDto(funding);

            return responseDto;

        }
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

        // 시스템 유저 키를 사용하여 펀딩 계좌 생성
        InquireAccountRequestDto inquireRequestDto = InquireAccountRequestDto.builder()
                .userKey(systemUserKey)
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
    public void useMoney(String authEmail, UseBudgetRequest useBudgetRequest) {

        // 로그인 유저가 펀딩의 주최자인지 확인
        User loginUser = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        Funding funding = fundingRepository.findById(useBudgetRequest.getFundingId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        if (!loginUser.equals(funding.getHost()))
            throw new ExceptionResponse(CustomException.INVALID_FUNDING_HOST_EXCEPTION);

        // request dto의 store에 펀딩 계좌에서 이체
        if (!transferToStore(useBudgetRequest))
            throw new ExceptionResponse(CustomException.TRANSFER_FAILURE_EXCEPTION);
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

        List<Funding> newFundings = new ArrayList<>();
        for (Funding funding : fundingEntities) {
            String fileName = imageUtil.extractFileName(funding.getFundingImage());
            funding.setFundingImage(fileName);

            newFundings.add(funding);
        }

        List<FundingResponseDto> fundingList = newFundings.stream()
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

        List<Funding> newFundings = new ArrayList<>();
        for (Funding funding : popularFundings) {
            String fileName = funding.getFundingImage();
            funding.setFundingImage(fileName);

            newFundings.add(funding);
        }

        // Funding 엔티티를 FundingResponseDto 로 변환
        return newFundings.stream()
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

        List<Funding> newFundings = new ArrayList<>();
        for (Funding funding : searchFundings) {
            String fileName = funding.getFundingImage();
            funding.setFundingImage(fileName);

            newFundings.add(funding);
        }

        return newFundings.stream()
                .map(FundingResponseDto::createResponseDto)
                .collect(Collectors.toList());
    }
    
    // 펀딩 참여 시 시스템 계좌에 이체
    private TransferJoinResponse transferFunding(TransferJoinRequest request) {

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
            System.out.println("Error Response: " + errorBody);

            webClientExceptionHandler.handleWebClientException(errorBody);

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

            return root.get("REC").get("accountBalance").asText();
        } catch (WebClientResponseException e) {
            // WebClient 오류 응답 처리
            String errorBody = e.getResponseBodyAsString();
            System.out.println("Error Response: " + errorBody);

            webClientExceptionHandler.handleWebClientException(errorBody);

        }catch (Exception e){
            throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
        }
        return null;
    }

    // 펀딩 계좌 잔액 기부
    public void donateRestAmount(DonateRequest request) {
        String url = "/edu/demandDeposit/updateDemandDepositAccountTransfer";

        CommonHeader header = CommonHeader.builder()
                .apiName("updateDemandDepositAccountTransfer")
                .apiServiceCode("updateDemandDepositAccountTransfer")
                .apiKey(apiKey)
                .userKey(systemUserKey)
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

            responseMono.block();


        } catch (WebClientResponseException e) {
            // WebClient 오류 응답 처리
            String errorBody = e.getResponseBodyAsString();
            System.out.println("Error Response: " + errorBody);

            webClientExceptionHandler.handleWebClientException(errorBody);

        }catch (Exception e){
            throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
        }
    }

    // 펀딩 사용처에 이체
    public boolean transferToStore(UseBudgetRequest request) {

        String url = "/edu/demandDeposit/updateDemandDepositAccountTransfer";

        CommonHeader header = CommonHeader.builder()
                .apiName("updateDemandDepositAccountTransfer")
                .apiServiceCode("updateDemandDepositAccountTransfer")
                .apiKey(apiKey)
                .userKey(systemUserKey)
                .build();
        header.setCommonHeader();

        Funding funding = fundingRepository.findById(request.getFundingId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        // 시스템 계좌에 참여자의 펀딩 참여 금액 입금
        TransferApiRequest apiRequest = TransferApiRequest.builder()
                .header(header)
                .depositAccountNo(request.getStoreAccount())
                .depositTransactionSummary(funding.getTitle() + " 입금")
                .transactionBalance(Integer.toString(request.getBalance()))
                .withdrawalAccountNo(funding.getAccount())
                .withdrawalTransactionSummary(request.getStoreSummary())
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
                return true;
            }

        } catch (WebClientResponseException e) {
            // WebClient 오류 응답 처리
            String errorBody = e.getResponseBodyAsString();
            System.out.println("Error Response: " + errorBody);

            webClientExceptionHandler.handleWebClientException(errorBody);

        }catch (Exception e){
            throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
        }

        return false;
    }

}
