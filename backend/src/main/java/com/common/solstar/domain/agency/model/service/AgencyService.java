package com.common.solstar.domain.agency.model.service;

import com.common.solstar.domain.funding.dto.response.FundingAgencyResponseDto;

import java.util.List;

public interface AgencyService {

    List<FundingAgencyResponseDto> getFundingList(String authEmail);

    void acceptFunding(int fundingId, String authEmail);

    void rejectFunding(int fundingId, String authEmail);
}
