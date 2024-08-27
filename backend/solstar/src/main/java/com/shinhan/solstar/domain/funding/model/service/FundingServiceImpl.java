package com.shinhan.solstar.domain.funding.model.service;

import com.shinhan.solstar.domain.artist.entity.Artist;
import com.shinhan.solstar.domain.artist.model.repository.ArtistRepository;
import com.shinhan.solstar.domain.funding.dto.request.FundingCreateRequestDto;
import com.shinhan.solstar.domain.funding.dto.request.FundingUpdateRequestDto;
import com.shinhan.solstar.domain.funding.dto.response.FundingContentResponseDto;
import com.shinhan.solstar.domain.funding.dto.response.FundingDetailResponseDto;
import com.shinhan.solstar.domain.funding.entity.Funding;
import com.shinhan.solstar.domain.funding.entity.FundingStatus;
import com.shinhan.solstar.domain.funding.entity.FundingType;
import com.shinhan.solstar.domain.funding.model.repository.FundingRepository;
import com.shinhan.solstar.domain.likeList.entity.LikeList;
import com.shinhan.solstar.domain.likeList.model.repository.LikeListRepository;
import com.shinhan.solstar.domain.user.entity.User;
import com.shinhan.solstar.domain.user.model.repository.UserRepository;
import com.shinhan.solstar.global.exception.CustomException;
import com.shinhan.solstar.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FundingServiceImpl implements FundingService {

    private final FundingRepository fundingRepository;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final LikeListRepository likeListRepository;

    @Override
    public void createFunding(FundingCreateRequestDto fundingDto) {

        Artist artist = artistRepository.findById(fundingDto.getArtistId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_ARTIST_EXCEPTION));
        // 임시로 User 생성하여 host 찾기
        // Security 구현 시 로그인한 유저를 가져오는 것으로 구현
        User host = userRepository.findById(1);

        // String 타입의 입력값을 FundingType으로 변환
        FundingType fundingType = FundingType.fromString(fundingDto.getType());

        Funding createdFunding = Funding.createFunding(fundingDto.getTitle(), fundingDto.getFundingImage(), fundingDto.getContent(), fundingDto.getGoalAmount(),
                fundingDto.getDeadlineDate(), 0, 0, artist, host, fundingType, FundingStatus.PROCESSING);

        fundingRepository.save(createdFunding);
    }

    @Override
    @Transactional
    public void updateFunding(int fundingId, FundingUpdateRequestDto fundingDto) {

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        funding.updateFundingDetails(fundingDto);
    }

    @Override
    @Transactional
    public void deleteFunding(int fundingId) {

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        funding.deleteFunding();
        fundingRepository.save(funding);
    }

    @Override
    public FundingDetailResponseDto getFundingById(int fundingId) {

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        // 펀딩 성공 시 상태 변경 및 펀딩 계좌 생성
        // 필요에 따라 기능 분리 (펀딩 참여 시 수정되도록  구현)
        if (funding.getGoalAmount() <= funding.getTotalAmount()) {
            funding.setStatus(FundingStatus.SUCCESS);

            fundingRepository.save(funding);
        }

        return FundingDetailResponseDto.createResponseDto(funding);
    }

    @Override
    public FundingContentResponseDto getFundingContent(int fundingId) {

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        return FundingContentResponseDto.createResponseDto(funding);
    }

    @Override
    public List<Funding> getMyLikeArtistFunding() {

        // 로그인한 유저 찾기
        User loginUser = userRepository.findById(1);

        List<LikeList> likeListEntities = likeListRepository.findByUser_Id(loginUser.getId());

        // LikeList 엔티티에서 Artist 엔티티 추출
        List<Artist> likeArtistEntities = likeListEntities.stream()
                .map(LikeList::getArtist)
                .collect(Collectors.toList());

        List<Funding> fundingList = fundingRepository.findByArtistIn(likeArtistEntities);

        return fundingList;
    }

}
