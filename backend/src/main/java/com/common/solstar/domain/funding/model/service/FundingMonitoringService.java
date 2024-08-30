package com.common.solstar.domain.funding.model.service;

import com.common.solstar.domain.account.entity.Account;
import com.common.solstar.domain.funding.dto.request.CreateDemandDepositAccountRequest;
import com.common.solstar.domain.funding.dto.request.TransferRefundRequest;
import com.common.solstar.domain.funding.dto.request.TransferTotalAmountRequest;
import com.common.solstar.domain.funding.dto.response.DemandDepositAccountResponse;
import com.common.solstar.domain.funding.dto.response.TransferRefundResponse;
import com.common.solstar.domain.funding.dto.response.TransferTotalAmountResponse;
import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.entity.FundingStatus;
import com.common.solstar.domain.funding.model.repository.FundingRepository;
import com.common.solstar.domain.fundingJoin.entity.FundingJoin;
import com.common.solstar.domain.fundingJoin.model.repository.FundingJoinRepository;
import com.common.solstar.domain.user.entity.User;
import com.common.solstar.global.api.request.CommonHeader;
import com.common.solstar.global.api.request.CreateDemandDepositAccountApiRequest;
import com.common.solstar.global.api.request.TransferApiRequest;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FundingMonitoringService {

    private final FundingRepository fundingRepository;
    private final FundingJoinRepository fundingJoinRepository;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://finopenapi.ssafy.io/ssafy/api/v1")
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ssafy.api.key}")
    private String apiKey;

    @Value("${account.type.unique.no}")
    private String accountTypeUniqueNo;

    @Value("${system.account.no}")
    private String systemAccountNo;

    // 매일 자정에 실행되도록 설정
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateFundingStatus() {
        // 진행 중이면서 마감일이 지난 펀딩들 조회
        List<Funding> closedFundings = fundingRepository.findByStatusAndDeadlineDateBefore(
                FundingStatus.PROCESSING, LocalDate.now().plusDays(1)
        );

        // 마감일이 지났는데 펀딩 성공(total >= goal) 상태인 경우
        for (Funding funding : closedFundings) {
            if (funding.getTotalAmount() >= funding.getGoalAmount()) {
                funding.setStatus(FundingStatus.SUCCESS);

                // 펀딩 계좌 생성해줘야 함
                CreateDemandDepositAccountRequest request = CreateDemandDepositAccountRequest.builder()
                        .userKey(funding.getHost().getUserKey())
                        .build();

                DemandDepositAccountResponse accountResponse = createFundingAccount(request);

                Account fundingAccount = Account.builder()
                        .accountNumber(accountResponse.getAccountNo())
                        .password(funding.getHost().getUserKey())
                        .bankCode(accountResponse.getBankCode())
                        .build();

                // 현재는 계좌번호만을 저장하는 방식
                funding.createAccount(fundingAccount.getAccountNumber());

                // 펀딩 계좌 생성 => 펀딩 계좌에 금액 이체에 시간적으로 딜레이가 필요한가?


                // 펀딩의 계좌에 totalAmount 넣어주기 위해 request dto 생성
                TransferTotalAmountRequest totalAmountRequest = TransferTotalAmountRequest.builder()
                        .userKey(funding.getHost().getUserKey())
                        .accountNo(fundingAccount.getAccountNumber())
                        .totalAmount(funding.getTotalAmount())
                        .build();

                // 펀딩 계좌에 이체
                transferTotalAmount(totalAmountRequest);

            } else {
                funding.setStatus(FundingStatus.CLOSED);

                // 환불해줘야 함 => 3시에 처리
            }
        }


        // 상태를 CLOSED로 변경
        for (Funding funding : closedFundings) {
            funding.setStatus(FundingStatus.CLOSED);
        }
        fundingRepository.saveAll(closedFundings);

        // 삭제 예정 펀딩들 조회 (마감일로부터 30일 지난 펀딩)
        List<Funding> deletableFundings = fundingRepository.findByStatusAndDeadlineDateBefore(FundingStatus.CLOSED, LocalDate.now().minusDays(30));

        // 삭제 처리
        for (Funding funding : deletableFundings) {
            funding.deleteFunding();
        }
        fundingRepository.saveAll(deletableFundings);
    }

    // 계좌 환불
    // 매일 오전 3시에 실행되도록 함
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void refund() {
        // 진행 중이면서 마감일이 지난 펀딩들 조회
        List<Funding> closedFundings = fundingRepository.findByStatusAndDeadlineDateBefore(
                FundingStatus.CLOSED, LocalDate.now().plusDays(1)
        );

        for (Funding funding : closedFundings) {
            List<FundingJoin> fundingJoinList = fundingJoinRepository.findByFunding(funding);

            // fundingJoin 하나하나 User를 꺼내서 user의 계좌에 amount 만큼 입금해줘야 함
            for (FundingJoin fundingJoin : fundingJoinList) {
                TransferRefundRequest request = TransferRefundRequest.builder()
                        .userKey(funding.getHost().getUserKey())
                        .fundingJoin(fundingJoin)
                        .build();

                // 시스템 계좌에서 사용자 계좌로 환불
                if (!transferRefund(request).isSuccess())
                    throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
            }
        }
    }


    // 펀딩 계좌 생성
    private DemandDepositAccountResponse createFundingAccount(CreateDemandDepositAccountRequest request) {

        String url = "/edu/demandDeposit/createDemandDepositAccount";

        CommonHeader header = CommonHeader.builder()
                .apiName("createDemandDepositAccount")
                .apiServiceCode("createDemandDepositAccount")
                .userKey(request.getUserKey())
                .apiKey(apiKey)
                .build();
        header.setCommonHeader();

        CreateDemandDepositAccountApiRequest apiRequest = CreateDemandDepositAccountApiRequest.builder()
                .header(header)
                .accountTypeUniqueNo(accountTypeUniqueNo)
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
            if (root.has("responseCode")) {
                throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
            }

            // response에서 계좌번호 저장하기
            if (root.has("REC")) {
                DemandDepositAccountResponse accountResponse = DemandDepositAccountResponse.builder()
                        .bankCode(root.get("REC").get("bankCode").asText())
                        .accountNo(root.get("REC").get("accountNo").asText())
                        .build();

                return accountResponse;
            }

        } catch (Exception e) {
            throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
        }
        return null;
    }

    // 펀딩 계좌 생성 시 시스템 계좌에서 펀딩의 총 모금액 이체
    public TransferTotalAmountResponse transferTotalAmount(TransferTotalAmountRequest request) {

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
                .depositAccountNo(request.getAccountNo())
                .depositTransactionSummary("펀딩 총 모금액 입금")
                .transactionBalance(Integer.toString(request.getTotalAmount()))
                .withdrawalAccountNo(systemAccountNo)
                .withdrawalTransactionSummary("펀딩 총 모금액 출금")
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

            if (root.has("REC")) {
                return TransferTotalAmountResponse.builder()
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

        return TransferTotalAmountResponse.builder()
                .isSuccess(false)
                .build();
    }

    // 환불 관련 계좌 이체
    public TransferRefundResponse transferRefund(TransferRefundRequest request) {

        String url = "/edu/demandDeposit/updateDemandDepositAccountTransfer";

        CommonHeader header = CommonHeader.builder()
                .apiName("updateDemandDepositAccountTransfer")
                .apiServiceCode("updateDemandDepositAccountTransfer")
                .userKey(request.getUserKey())
                .apiKey(apiKey)
                .build();
        header.setCommonHeader();

        FundingJoin fundingJoin = request.getFundingJoin();

        User participant = fundingJoin.getUser();

        TransferApiRequest apiRequest = TransferApiRequest.builder()
                .header(header)
                .depositAccountNo(participant.getAccount().getAccountNumber())
                .depositTransactionSummary("펀딩 취소 환불 입금")
                .transactionBalance(Integer.toString(fundingJoin.getAmount()))
                .withdrawalAccountNo(systemAccountNo)
                .withdrawalTransactionSummary("펀딩 취소 환불 입금")
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
                return TransferRefundResponse.builder()
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

        return TransferRefundResponse.builder()
                .isSuccess(false)
                .build();
    }

}
