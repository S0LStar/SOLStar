package com.shinhan.solstar.domain.artist.model.service;

import com.shinhan.solstar.domain.artist.dto.response.ArtistResponseDto;
import com.shinhan.solstar.domain.artist.dto.response.LikeArtistResponseDto;
import com.shinhan.solstar.domain.artist.entity.Artist;
import com.shinhan.solstar.domain.artist.model.repository.ArtistRepository;
import com.shinhan.solstar.domain.funding.dto.response.FundingResponseDto;
import com.shinhan.solstar.domain.funding.entity.Funding;
import com.shinhan.solstar.domain.funding.model.repository.FundingRepository;
import com.shinhan.solstar.domain.likeList.model.repository.LikeListRepository;
import com.shinhan.solstar.domain.user.entity.User;
import com.shinhan.solstar.domain.user.model.repository.UserRepository;
import com.shinhan.solstar.global.exception.CustomException;
import com.shinhan.solstar.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final FundingRepository fundingRepository;
    private final UserRepository userRepository;
    private final LikeListRepository likeListRepository;

    @Override
    public ArtistResponseDto getArtistById(int artistId) {

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_ARTIST_EXCEPTION));

        List<Funding> fundingEntities = fundingRepository.findByArtist_Id(artistId);

        List<FundingResponseDto> fundingList = fundingEntities.stream()
                .map(funding -> FundingResponseDto.createResponseDto(funding))
                .collect(Collectors.toList());

        return ArtistResponseDto.createResponseDto(artist, fundingList);
    }

    @Override
    public List<LikeArtistResponseDto> getLikeArtist() {
        // 로그인한 유저가 좋아하는 artist의 id 찾아오기
        // 임시로 id = 1 인 유저 호출
        User loginUser = userRepository.findById(1);
        List<Artist> likeArtistEntities = likeListRepository.findByUser_Id(loginUser.getId());

        List<LikeArtistResponseDto> likeArtistList = likeArtistEntities.stream()
                .map(artist -> LikeArtistResponseDto.createResponseDto(artist))
                .collect(Collectors.toList());

        return likeArtistList;
    }

    @Override
    public void like(int artistId) {
        User loginUser = userRepository.findById(1);
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_ARTIST_EXCEPTION));

//        LikeList likeArtist = new LikeList(loginUser, artist);
    }
}
