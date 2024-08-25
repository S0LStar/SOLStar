package com.shinhan.solstar.domain.funding.model.service;

import com.shinhan.solstar.domain.funding.model.repository.FundingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FundingServiceImpl implements FundingService {

    private final FundingRepository fundingRepository;

}
