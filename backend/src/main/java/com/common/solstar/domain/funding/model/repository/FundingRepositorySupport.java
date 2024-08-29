package com.common.solstar.domain.funding.model.repository;

import com.common.solstar.domain.funding.entity.QFunding;
import com.common.solstar.domain.user.dto.response.HostFundingResponse;
import com.common.solstar.domain.user.dto.response.UserJoinFundingReponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.common.solstar.domain.fundingJoin.entity.QFundingJoin.fundingJoin;

@Repository
@RequiredArgsConstructor
public class FundingRepositorySupport {

    private JPAQueryFactory jpaQueryFactory;

    // 유저 아이디로 주최 펀딩 리스트 조회
    public List<HostFundingResponse> findFundingByHostId(int hostId) {
        QFunding funding = QFunding.funding;

        return (List<HostFundingResponse>) jpaQueryFactory
                .select(Projections.constructor(HostFundingResponse.class,
                        funding.id,
                        funding.artist.name,
                        funding.title,
                        funding.status,
                        funding.totalAmount,
                        funding.goalAmount,
                        funding.type,
                        funding.fundingImage))
                .from(funding)
                .where(funding.host.id.eq(hostId))
                .fetch();
    }

    // 유저 아이디로 주최 펀딩의 계좌 정보 조회
    public List<String> findFundingAccountByHostId(int hostId) {
        QFunding funding = QFunding.funding;
        return (List<String>) jpaQueryFactory
                .select(
                        funding.account
                )
                .from(funding)
                .where(funding.host.id.eq(hostId))
                .fetch();

    }


}
