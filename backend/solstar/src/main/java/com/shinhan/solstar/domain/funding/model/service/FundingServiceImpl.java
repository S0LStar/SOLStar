package com.shinhan.solstar.domain.funding.model.service;

import com.shinhan.solstar.domain.funding.dto.FundingCreateRequestDto;
import com.shinhan.solstar.domain.funding.entity.Funding;
import com.shinhan.solstar.domain.funding.model.repository.FundingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FundingServiceImpl implements FundingService {

    private final FundingRepository fundingRepository;

    @Override
    public void createFunding(FundingCreateRequestDto fundingDto) {

//        Funding createdFunding = Funding.createFunding()

//        fundingRepository.save();
    }

}
