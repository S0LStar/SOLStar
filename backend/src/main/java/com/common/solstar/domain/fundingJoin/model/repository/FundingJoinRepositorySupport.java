package com.common.solstar.domain.fundingJoin.model.repository;

import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.entity.QFunding;
import com.common.solstar.domain.fundingJoin.entity.QFundingJoin;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FundingJoinRepositorySupport {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    // 유저가 특정 펀딩에 참여자인지 아닌지 확인
    public boolean isJoinFunding(int fundingId, int userId){
        QFundingJoin fundingJoin = QFundingJoin.fundingJoin;
        Integer count = Math.toIntExact(jpaQueryFactory
                .select(fundingJoin.count())
                .from(fundingJoin)
                .where(fundingJoin.funding.id.eq(fundingId)
                        .and(fundingJoin.user.id.eq(userId)))
                .fetchOne());
        if(count==0) return false;
        return true;
    }


}
