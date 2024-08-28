package com.common.solstar.domain.fundingJoin.model.repository;

import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.fundingJoin.entity.FundingJoin;
import com.common.solstar.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundingJoinRepository extends JpaRepository<FundingJoin, Integer> {
    boolean existsByUserAndFunding(User user, Funding funding);
}
