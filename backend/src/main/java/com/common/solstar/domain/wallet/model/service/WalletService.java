package com.common.solstar.domain.wallet.model.service;

import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.model.repository.FundingRepository;
import com.common.solstar.domain.funding.model.repository.FundingRepositorySupport;
import com.common.solstar.domain.user.entity.User;
import com.common.solstar.domain.user.model.repository.UserRepository;
import com.common.solstar.domain.wallet.dto.response.FindMyAccountReponse;
import com.common.solstar.domain.wallet.dto.response.FindMyHostFundingAccountResponse;
import com.common.solstar.domain.wallet.dto.response.FundingTransactionHistoryResponse;
import com.common.solstar.global.api.request.CommonHeader;
import com.common.solstar.global.api.request.FindAccountApiRequest;
import com.common.solstar.global.auth.jwt.JwtUtil;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://finopenapi.ssafy.io/ssafy/api/v1")
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FundingRepositorySupport fundingRepositorySupport;
    private final FundingRepository fundingRepository;

    @Value("${ssafy.api.key}")
    private String apiKey;

    @Value("${ssafy.solstar.system.user.key}")
    private String systemUserKey;

    // 나의 연동 계좌 조회
    public FindMyAccountReponse getMyWallet(String header) {
        String authEmail = jwtUtil.getLoginUser(header);

        if (authEmail == null) {
            throw new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION);
        }
        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        String url = "/edu/demandDeposit/inquireDemandDepositAccountBalance";

        CommonHeader commonHeader = CommonHeader.builder()
                .apiName("inquireDemandDepositAccountBalance")
                .apiServiceCode("inquireDemandDepositAccountBalance")
                .apiKey(apiKey)
                .userKey(user.getUserKey())
                .build();
        commonHeader.setCommonHeader();
        FindAccountApiRequest apiRequest = FindAccountApiRequest.builder()
                .accountNo(user.getAccount().getAccountNumber())
                .header(commonHeader)
                .build();

        System.out.println(user.getAccount().getAccountNumber());

        try{
            String jsonRequestBody = objectMapper.writeValueAsString(apiRequest);
            System.out.println("Request JSON: " + jsonRequestBody);  // JSON 바디 출력

            // API 요청 보내기
            Mono<String> responseMono = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .bodyValue(apiRequest)
                    .retrieve()
                    .bodyToMono(String.class);

            String response = responseMono.block();
            JsonNode root = objectMapper.readTree(response);

            return FindMyAccountReponse.builder()
                    .accountBalance(root.get("REC").get("accountBalance").asText())
                    .userName(user.getName())
                    .accountNo(user.getAccount().getAccountNumber())
                    .build();
        } catch (WebClientResponseException | JsonProcessingException e) {


            throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);

            }
    }

    // 내가 주최하는 펀딩의 계좌 조회
    public List<FindMyHostFundingAccountResponse> getMyHostFundingAccount(String header) {
        String authEmail = jwtUtil.getLoginUser(header);
        if (authEmail == null) {
            throw new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION);
        }
        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        // 주최자 아이디가 유저 아이디랑 같은 펀딩의 리스트
        List<Funding> hostFundingList = fundingRepository.findByHostId(user.getId());

        String url = "/edu/demandDeposit/inquireDemandDepositAccountBalance";
        List<FindMyHostFundingAccountResponse> responseList = new ArrayList<>();

        // 각 펀딩에 대해 API 호출을 통해 계좌 정보를 가져옴
        for (Funding funding : hostFundingList) {
            // API 요청에 필요한 공통 헤더를 설정
            CommonHeader commonHeader = CommonHeader.builder()
                    .apiName("inquireDemandDepositAccountBalance")
                    .apiServiceCode("inquireDemandDepositAccountBalance")
                    .apiKey(apiKey)
                    .userKey(user.getUserKey())
                    .build();
            commonHeader.setCommonHeader();

            // API 요청 데이터 생성
            FindAccountApiRequest apiRequest = FindAccountApiRequest.builder()
                    .accountNo(funding.getAccount())  // 펀딩에 연결된 계좌 번호를 가져옴
                    .header(commonHeader)
                    .build();

            try {
                // 요청 데이터를 JSON으로 변환
                String jsonRequestBody = objectMapper.writeValueAsString(apiRequest);
                System.out.println("Request JSON: " + jsonRequestBody);

                // API 요청 보내기
                Mono<String> responseMono = webClient.post()
                        .uri(url)
                        .header("Content-Type", "application/json")
                        .bodyValue(apiRequest)
                        .retrieve()
                        .bodyToMono(String.class);

                // API 응답을 블록하여 받음
                String response = responseMono.block();
                JsonNode root = objectMapper.readTree(response);

                // 응답 데이터를 리스트에 추가
                responseList.add(FindMyHostFundingAccountResponse.builder()
                        .fundingId(funding.getId())
                        .accountNo(funding.getAccount())
                        .accountBalance(root.get("REC").get("accountBalance").asText())
                        .artistName(funding.getArtist().getName())
                        .userName(user.getName())
                        .build());

            } catch (WebClientResponseException | JsonProcessingException e) {
                // 예외 발생 시 사용자 정의 예외를 던짐
                throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
            }
        }

        return responseList;
    }

//    public List<FundingTransactionHistoryResponse> getFundingTransactionHistory(String header) {
//    }
}
