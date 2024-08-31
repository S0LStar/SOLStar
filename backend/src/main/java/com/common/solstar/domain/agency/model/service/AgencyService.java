package com.common.solstar.domain.agency.model.service;

import com.common.solstar.domain.agency.dto.request.UpdateNameRequest;
import com.common.solstar.domain.agency.dto.request.UpdateProfileImageRequest;
import com.common.solstar.domain.agency.dto.response.AgencyDetailResponse;
import com.common.solstar.domain.funding.dto.response.FundingAgencyResponseDto;

import java.util.List;

public interface AgencyService {

    List<FundingAgencyResponseDto> getFundingList(String authEmail);

    void acceptFunding(int fundingId, String authEmail);

    void rejectFunding(int fundingId, String authEmail);

    void updateProfileImage(String authEmail, UpdateProfileImageRequest request);

    void updateName(String authEmail, UpdateNameRequest request);

    AgencyDetailResponse getAgencyDetail(String authEmail);

    void logout(String authEmail);
}
