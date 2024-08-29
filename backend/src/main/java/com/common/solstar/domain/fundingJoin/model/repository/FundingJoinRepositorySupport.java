package com.common.solstar.domain.fundingJoin.model.repository;

import com.common.solstar.domain.funding.entity.QFunding;
import com.common.solstar.domain.fundingJoin.entity.QFundingJoin;
import com.common.solstar.domain.user.dto.response.UserJoinFundingReponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FundingJoinRepositorySupport {

    private JPAQueryFactory jpaQueryFactory;

    public List<UserJoinFundingReponse> findFundingByUserEmail(int id) {
        QFundingJoin fundingJoin = QFundingJoin.fundingJoin;
        QFunding funding = QFunding.funding;

        return jpaQueryFactory
                .select(Projections.constructor(UserJoinFundingReponse.class,
                        funding.id,
                        funding.artist.name,
                        funding.title,
                        funding.status,
                        funding.totalAmount,
                        funding.goalAmount,
                        funding.type,
                        funding.fundingImage))
                .from(fundingJoin)
                .join(fundingJoin.funding, funding)
                .where(fundingJoin.user.id.eq(id))
                .fetch();
    }


}
