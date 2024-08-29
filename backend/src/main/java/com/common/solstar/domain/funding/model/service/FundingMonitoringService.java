package com.common.solstar.domain.funding.model.service;

import com.common.solstar.domain.account.entity.Account;
import com.common.solstar.domain.account.model.repository.AccountRepository;
import com.common.solstar.domain.funding.dto.request.CreateDemandDepositAccountRequest;
import com.common.solstar.domain.funding.dto.response.DemandDepositAccountResponse;
import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.entity.FundingStatus;
import com.common.solstar.domain.funding.model.repository.FundingRepository;
import com.common.solstar.global.api.request.CommonHeader;
import com.common.solstar.global.api.request.CreateDemandDepositAccountApiRequest;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FundingMonitoringService {

    private final FundingRepository fundingRepository;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://finopenapi.ssafy.io/ssafy/api/v1")
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AccountRepository accountRepository;

    @Value("${ssafy.api.key}")
    private String apiKey;

    @Value("${account.type.unique.no}")
    private String accountTypeUniqueNo;

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
            } else {
                funding.setStatus(FundingStatus.CLOSED);

                // 환불해줘야 함 => 2시에 처리
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

    // 계좌 생성
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

}
