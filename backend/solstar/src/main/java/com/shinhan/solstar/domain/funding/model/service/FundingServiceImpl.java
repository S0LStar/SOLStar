package com.shinhan.solstar.domain.funding.model.service;

import com.shinhan.solstar.domain.artist.entity.Artist;
import com.shinhan.solstar.domain.artist.model.repository.ArtistRepository;
import com.shinhan.solstar.domain.funding.dto.FundingCreateRequestDto;
import com.shinhan.solstar.domain.funding.entity.Funding;
import com.shinhan.solstar.domain.funding.entity.FundingStatus;
import com.shinhan.solstar.domain.funding.entity.FundingType;
import com.shinhan.solstar.domain.funding.model.repository.FundingRepository;
import com.shinhan.solstar.domain.user.entity.User;
import com.shinhan.solstar.domain.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FundingServiceImpl implements FundingService {

    private final FundingRepository fundingRepository;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;

    @Override
    public void createFunding(FundingCreateRequestDto fundingDto) {

        Artist artist = artistRepository.findById(fundingDto.getArtistId());
//        User host = userRepository.findById();
        User host = new User();

        // String 타입의 입력값을 FundingType으로 변환
        FundingType fundingType = FundingType.fromString(fundingDto.getType());

        Funding createdFunding = Funding.createFunding(fundingDto.getTitle(), fundingDto.getFundingImage(), fundingDto.getContent(), fundingDto.getContentImage(), fundingDto.getGoalAmount(),
                fundingDto.getDeadlineDate(), 0, 0, artist, host, fundingType, FundingStatus.PROCESSING);

        fundingRepository.save(createdFunding);
    }

}
