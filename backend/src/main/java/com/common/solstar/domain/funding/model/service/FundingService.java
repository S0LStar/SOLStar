package com.common.solstar.domain.funding.model.service;

import com.common.solstar.domain.funding.dto.request.FundingCreateRequestDto;
import com.common.solstar.domain.funding.dto.request.FundingUpdateRequestDto;
import com.common.solstar.domain.funding.dto.response.FundingContentResponseDto;
import com.common.solstar.domain.funding.dto.response.FundingDetailResponseDto;
import com.common.solstar.domain.funding.dto.response.FundingResponseDto;
import com.common.solstar.domain.fundingJoin.dto.request.FundingJoinCreateRequestDto;

import java.util.List;

public interface FundingService {

    // 펀딩 생성
    void createFunding(FundingCreateRequestDto fundingDto, String authEmail);

    // 펀딩 수정
    void updateFunding(int fundingId, FundingUpdateRequestDto fundingDto, String authEmail);

    // 펀딩 삭제
    void deleteFunding(int fundingId, String authEmail);

    // 펀딩 상세 조회
    FundingDetailResponseDto getFundingById(int fundingId, String authEmail);

    // 펀딩 내용 조회
    FundingContentResponseDto getFundingContent(int fundingId, String authEmail);

    // 펀딩 참여
    void joinFunding(FundingJoinCreateRequestDto joinFundingDto, String authEmail);

    // 내가 선호하는 아티스트 펀딩 조회
    List<FundingResponseDto> getMyLikeArtistFunding(String authEmail);

    // 최근 인기 펀딩 조회
    List<FundingResponseDto> getPopularFunding(String authEmail);

    // 펀딩 검색 조회
    List<FundingResponseDto> getSearchFunding(String keyword, String authEmail);
}
