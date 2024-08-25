package com.shinhan.solstar.domain.funding.model.repository;

import com.shinhan.solstar.domain.funding.entity.Funding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundingRepository extends JpaRepository<Funding, Integer> {

}
