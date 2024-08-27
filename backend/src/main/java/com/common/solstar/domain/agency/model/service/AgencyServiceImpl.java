package com.common.solstar.domain.agency.model.service;

import com.common.solstar.domain.agency.entity.Agency;
import com.common.solstar.domain.agency.model.repository.AgencyRepository;
import com.common.solstar.domain.funding.dto.response.FundingAgencyResponseDto;
import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.model.repository.FundingRepository;
import com.common.solstar.domain.fundingAgency.entity.FundingAgency;
import com.common.solstar.domain.fundingAgency.model.repository.FundingAgencyRepository;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AgencyServiceImpl implements AgencyService {

    private final FundingRepository fundingRepository;
    private final FundingAgencyRepository fundingAgencyRepository;
    private final AgencyRepository agencyRepository;

    @Override
    public List<FundingAgencyResponseDto> getFundingList() {

        // 로그인한 소속사 정보 불러오기
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

    @Override
    @Transactional
    public void acceptFunding(int fundingId) {

        // 로그인한 소속사 정보 불러오기
        Agency agency = agencyRepository.findById(1)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AGENCY_EXCEPTION));

        // 펀딩의 아티스트의 소속사가 현재 로그인한 소속사가 맞는지 검증
        Funding selectedFunding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        FundingAgency matchConnection = fundingAgencyRepository.findByFunding(selectedFunding)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_AGENCY_EXCEPTION));

        if (!matchConnection.getAgency().equals(agency))
            throw new ExceptionResponse(CustomException.NOT_MATCH_AGENCY_EXCEPTION);

        // 미승인 상태라면, 승인 상태로 수정
        if (!matchConnection.isStatus()) {
            matchConnection.acceptFunding();
        } else {
            throw new ExceptionResponse(CustomException.ALREADY_ACCEPT_FUNDING_EXCEPTION);
        }
    }

    @Override
    @Transactional
    public void rejectFunding(int fundingId) {

        // 로그인한 소속사 정보 불러오기
        Agency agency = agencyRepository.findById(1)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AGENCY_EXCEPTION));

        // 펀딩의 아티스트의 소속사가 현재 로그인한 소속사가 맞는지 검증
        Funding selectedFunding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        FundingAgency matchConnection = fundingAgencyRepository.findByFunding(selectedFunding)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_AGENCY_EXCEPTION));

        if (!matchConnection.getAgency().equals(agency))
            throw new ExceptionResponse(CustomException.NOT_MATCH_AGENCY_EXCEPTION);

        // 펀딩 인증 요청이 거절되면 해당 펀딩은 삭제 처리
        if (!matchConnection.isStatus()) {
            matchConnection.getFunding().deleteFunding();
        } else {
            throw new ExceptionResponse(CustomException.ALREADY_ACCEPT_FUNDING_EXCEPTION);
        }
    }

}