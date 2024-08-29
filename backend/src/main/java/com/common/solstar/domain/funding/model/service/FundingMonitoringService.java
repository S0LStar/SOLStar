package com.common.solstar.domain.funding.model.service;

import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.entity.FundingStatus;
import com.common.solstar.domain.funding.model.repository.FundingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FundingMonitoringService {

    private final FundingRepository fundingRepository;

    // 매일 자정에 실행되도록 설정
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateFundingStatus() {
        // 진행 중이면서 마감일이 지난 펀딩들 조회
        List<Funding> closedFundings = fundingRepository.findByStatusAndDeadlineDateBefore(FundingStatus.PROCESSING, LocalDate.now());

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

}
