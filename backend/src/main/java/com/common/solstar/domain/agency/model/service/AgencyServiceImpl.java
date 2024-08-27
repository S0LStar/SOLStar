package com.common.solstar.domain.agency.model.service;

import com.common.solstar.domain.agency.entity.Agency;
import com.common.solstar.domain.agency.model.repository.AgencyRepository;
import com.common.solstar.domain.funding.dto.response.FundingAgencyResponseDto;
import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.fundingAgency.entity.FundingAgency;
import com.common.solstar.domain.fundingAgency.model.repository.FundingAgencyRepository;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AgencyServiceImpl implements AgencyService {

    private final FundingAgencyRepository fundingAgencyRepository;
    private final AgencyRepository agencyRepository;

    public List<FundingAgencyResponseDto> getFundingList() {

        // 로그인한 사용자의 agency 정보 불러오기
        Agency agency = agencyRepository.findById(1)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AGENCY_EXCEPTION));

        // status가 false(승인 대기중)인 펀딩 - 소속사 연결관계만 추출
        List<FundingAgency> fundingAgencies = fundingAgencyRepository.findByAgencyAndStatusFalse(agency);

        // FundingAgency 엔티티에서 Funding 엔티티 추출
        List<Funding> fundingEntities = fundingAgencies.stream()
                .map(FundingAgency::getFunding)
                .collect(Collectors.toList());

        List<FundingAgencyResponseDto> fundingList = fundingEntities.stream()
                .map(funding -> FundingAgencyResponseDto.createResponseDto(funding))
                .collect(Collectors.toList());

        return fundingList;
    }

}