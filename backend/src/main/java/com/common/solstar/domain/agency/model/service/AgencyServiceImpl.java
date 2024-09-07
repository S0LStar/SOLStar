package com.common.solstar.domain.agency.model.service;

import com.common.solstar.domain.agency.dto.request.UpdateNameRequest;
import com.common.solstar.domain.agency.dto.request.UpdateProfileImageRequest;
import com.common.solstar.domain.agency.dto.response.AgencyDetailResponse;
import com.common.solstar.domain.agency.entity.Agency;
import com.common.solstar.domain.agency.model.repository.AgencyRepository;
import com.common.solstar.domain.funding.dto.response.FundingAgencyResponseDto;
import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.entity.FundingStatus;
import com.common.solstar.domain.funding.model.repository.FundingRepository;
import com.common.solstar.domain.fundingAgency.entity.FundingAgency;
import com.common.solstar.domain.fundingAgency.model.repository.FundingAgencyRepository;
import com.common.solstar.domain.user.entity.User;
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
    public List<FundingAgencyResponseDto> getFundingList(String authEmail) {

        // 로그인한 소속사 정보 불러오기
        Agency agency = agencyRepository.findByEmail(authEmail)
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
    public void acceptFunding(int fundingId, String authEmail) {

        // 로그인한 소속사 정보 불러오기
        Agency agency = agencyRepository.findByEmail(authEmail)
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
            selectedFunding.updateStatusProcessing();

            fundingAgencyRepository.save(matchConnection);
            fundingRepository.save(selectedFunding);
        } else {
            throw new ExceptionResponse(CustomException.ALREADY_ACCEPT_FUNDING_EXCEPTION);
        }
    }

    @Override
    @Transactional
    public void rejectFunding(int fundingId, String authEmail) {

        // 로그인한 소속사 정보 불러오기
        Agency agency = agencyRepository.findByEmail(authEmail)
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
    
    // 프로필 이미지 수정
    @Override
    @Transactional
    public void updateProfileImage(String authEmail, UpdateProfileImageRequest request) {
        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        Agency agency = agencyRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AGENCY_EXCEPTION));

        if(request.getProfileImage() == null) {
            agency.deleteProfileImage();
        }
        else agency.updateProfileImage(request.getProfileImage());
        agencyRepository.save(agency);
    }

    // 이름 수정
    @Override
    public void updateName(String authEmail, UpdateNameRequest request) {

        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        Agency agency = agencyRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AGENCY_EXCEPTION));

        // 이미 가입된 이름이라면 불가능
        if(agencyRepository.existsByName(request.getName())){
            throw new ExceptionResponse(CustomException.DUPLICATED_NAME_EXCEPTION);
        }

        agency.updateName(request.getName());
        agencyRepository.save(agency);

    }

    // 소속사 정보 조회
    @Override
    public AgencyDetailResponse getAgencyDetail(String authEmail) {
        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        Agency agency = agencyRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AGENCY_EXCEPTION));

        return AgencyDetailResponse.builder()
                .profileImage(agency.getProfileImage())
                .name(agency.getName())
                .phone(agency.getPhone())
                .email(agency.getEmail())
                .build();
    }

    // 로그아웃
    @Override
    @Transactional
    public void logout(String authEmail) {
        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        Agency agency = agencyRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_AGENCY_EXCEPTION));

        agency.deleteRefreshToken();
        agencyRepository.save(agency);
    }



}