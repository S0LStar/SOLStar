package com.common.solstar.domain.funding.model.service;

import com.common.solstar.domain.account.entity.Account;
import com.common.solstar.domain.account.model.repository.AccountRepository;
import com.common.solstar.domain.artist.entity.Artist;
import com.common.solstar.domain.artist.model.repository.ArtistRepository;
import com.common.solstar.domain.funding.dto.request.CreateDemandDepositAccountRequest;
import com.common.solstar.domain.funding.dto.request.FundingCreateRequestDto;
import com.common.solstar.domain.funding.dto.request.FundingUpdateRequestDto;
import com.common.solstar.domain.funding.dto.response.DemandDepositAccountResponse;
import com.common.solstar.domain.funding.dto.response.FundingContentResponseDto;
import com.common.solstar.domain.funding.dto.response.FundingDetailResponseDto;
import com.common.solstar.domain.funding.dto.response.FundingResponseDto;
import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.entity.FundingStatus;
import com.common.solstar.domain.funding.entity.FundingType;
import com.common.solstar.domain.funding.model.repository.FundingRepository;
import com.common.solstar.domain.fundingAgency.entity.FundingAgency;
import com.common.solstar.domain.fundingAgency.model.repository.FundingAgencyRepository;
import com.common.solstar.domain.fundingJoin.dto.request.FundingJoinCreateRequestDto;
import com.common.solstar.domain.fundingJoin.entity.FundingJoin;
import com.common.solstar.domain.fundingJoin.model.repository.FundingJoinRepository;
import com.common.solstar.domain.likeList.entity.LikeList;
import com.common.solstar.domain.likeList.model.repository.LikeListRepository;
import com.common.solstar.domain.user.entity.User;
import com.common.solstar.domain.user.model.repository.UserRepository;
import com.common.solstar.global.api.request.CommonHeader;
import com.common.solstar.global.api.request.CreateDemandDepositAccountApiRequest;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FundingServiceImpl implements FundingService {

    private final FundingRepository fundingRepository;
    private final FundingAgencyRepository fundingAgencyRepository;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final LikeListRepository likeListRepository;
    private final FundingJoinRepository fundingJoinRepository;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://finopenapi.ssafy.io/ssafy/api/v1")
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AccountRepository accountRepository;

    @Value("${ssafy.api.key}")
    private String apiKey;

    @Value("${account.type.unique.no}")
    private String accountTypeUniqueNo;

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

        if (createdFunding.getType().equals(FundingType.VERIFIED)) {
            FundingAgency fundingAgency = FundingAgency.createFundingAgency(createdFunding, createdFunding.getArtist().getAgency());

            fundingAgencyRepository.save(fundingAgency);
        }
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

        // 로그인한 유저 찾기
        User loginUser = userRepository.findById(2);

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        FundingDetailResponseDto responseDto = FundingDetailResponseDto.createResponseDto(funding);

        if (funding.getHost().equals(loginUser)) {
            // 로그인한 유저가 주최자인 경우
            responseDto.setJoinStatus(2);
        } else if (fundingJoinRepository.existsByUserAndFunding(loginUser, funding)) {
            // 로그인한 유저가 참여자인 경우
            responseDto.setJoinStatus(1);
        }

        return responseDto;
    }

    @Override
    public FundingContentResponseDto getFundingContent(int fundingId) {

        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        return FundingContentResponseDto.createResponseDto(funding);
    }

    @Override
    @Transactional
    public void joinFunding(FundingJoinCreateRequestDto joinFundingDto) {

        // 로그인한 유저 찾기
        User loginUser = userRepository.findById(2);

        // 해당 유저가 소유한 계좌가 있는지 확인
        Account account = loginUser.getAccount();

        // 비밀번호가 맞는지 확인
        if (!joinFundingDto.getPassword().equals(account.getPassword())) {
            throw new ExceptionResponse(CustomException.NOT_MATCH_ACCOUNT_PASSWORD_EXCEPTION);
        }

        // 펀딩에 대한 유효성 검사
        // 펀딩 존재 유무
        Funding funding = fundingRepository.findById(joinFundingDto.getFundingId())
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_FUNDING_EXCEPTION));

        // 펀딩이 진행중인 펀딩인지 확인
        if (!funding.getStatus().equals(FundingStatus.PROCESSING))
            throw new ExceptionResponse(CustomException.NOT_PROCESSING_FUNDING_EXCEPTION);

        // FundingJoin 객체 만들어서 저장
        FundingJoin fundingJoin = new FundingJoin(joinFundingDto.getFundingId(), loginUser, funding, joinFundingDto.getAmount(), LocalDateTime.now());

        fundingJoinRepository.save(fundingJoin);

        // totalAmount와 totalJoin 업데이트
        funding.updateByJoin(fundingJoin.getAmount());

        fundingRepository.save(funding);
    }

    @Override
    public List<FundingResponseDto> getMyLikeArtistFunding() {

        // 로그인한 유저 찾기
        User loginUser = userRepository.findById(1);

        List<LikeList> likeListEntities = likeListRepository.findByUser_Id(loginUser.getId());

        // LikeList 엔티티에서 Artist 엔티티 추출
        List<Artist> likeArtistEntities = likeListEntities.stream()
                .map(LikeList::getArtist)
                .collect(Collectors.toList());

        List<Funding> fundingEntities = fundingRepository.findByArtistIn(likeArtistEntities);

        List<FundingResponseDto> fundingList = fundingEntities.stream()
                .map(funding -> FundingResponseDto.createResponseDto(funding))
                .collect(Collectors.toList());

        return fundingList;
    }

    @Override
    public List<FundingResponseDto> getPopularFunding() {

        // 로그인한 유저 찾기
        User loginUser = userRepository.findById(1);

        // totalJoin 기준으로 내림차순 정렬된 펀딩 목록을 가져옴
        List<Funding> popularFundings = fundingRepository.findPopularFundings();

        // Funding 엔티티를 FundingResponseDto 로 변환
        return popularFundings.stream()
                .map(FundingResponseDto::createResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FundingResponseDto> getSearchFunding(String keyword) {

        // 로그인한 유저 불러오기
        User loginUser = userRepository.findById(1);

        // 검색어로 펀딩 리스트 불러오기
        List<Funding> searchFundings = fundingRepository.findByTitleContainingIgnoreCase(keyword);

        return searchFundings.stream()
                .map(FundingResponseDto::createResponseDto)
                .collect(Collectors.toList());
    }
    
    // 펀딩 참여 시 시스템 계좌에 이체

}
