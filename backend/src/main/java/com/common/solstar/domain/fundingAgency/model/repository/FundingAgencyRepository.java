package com.common.solstar.domain.fundingAgency.model.repository;

import com.common.solstar.domain.agency.entity.Agency;
import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.fundingAgency.entity.FundingAgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FundingAgencyRepository extends JpaRepository<FundingAgency, Integer> {

    List<FundingAgency> findByAgencyAndStatusFalse(Agency agency);

    Optional<FundingAgency> findByAgency(Agency agency);

    Optional<FundingAgency> findByFunding(Funding funding);
}
