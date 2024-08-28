package com.common.solstar.domain.fundingJoin.model.repository;

import com.common.solstar.domain.fundingJoin.entity.FundingJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundingJoinRepository extends JpaRepository<FundingJoin, Integer> {
}
