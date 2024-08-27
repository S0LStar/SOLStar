package com.shinhan.solstar.domain.artist.model.service;

import com.shinhan.solstar.domain.artist.dto.response.ArtistResponseDto;
import com.shinhan.solstar.domain.artist.dto.response.ArtistSearchResponseDto;
import com.shinhan.solstar.domain.artist.dto.response.LikeArtistResponseDto;
import com.shinhan.solstar.domain.artist.entity.Artist;
import com.shinhan.solstar.domain.artist.model.repository.ArtistRepository;
import com.shinhan.solstar.domain.funding.dto.response.FundingResponseDto;
import com.shinhan.solstar.domain.funding.entity.Funding;
import com.shinhan.solstar.domain.funding.model.repository.FundingRepository;
import com.shinhan.solstar.domain.likeList.entity.LikeList;
import com.shinhan.solstar.domain.likeList.model.repository.LikeListRepository;
import com.shinhan.solstar.domain.user.entity.User;
import com.shinhan.solstar.domain.user.model.repository.UserRepository;
import com.shinhan.solstar.global.exception.CustomException;
import com.shinhan.solstar.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final FundingRepository fundingRepository;
    private final UserRepository userRepository;
    private final LikeListRepository likeListRepository;

    @Override
    public List<ArtistSearchResponseDto> searchArtists(String keyword) {
        // 로그인한 유저 불러오기
        User loginUser = userRepository.findById(1);

        // 검색어로 아티스트 리스트 불러오기
        List<Artist> artists = artistRepository.findByNameContainingIgnoreCaseOrGroupContainingIgnoreCase(keyword, keyword);

        // 아티스트마다 찜 여부 확인 후, dto로 변환
        List<ArtistSearchResponseDto> artistSearchResponseList = artists.stream()
                .map(artist -> {
                    boolean isLiked = likeListRepository.existsByUserAndArtist(loginUser, artist);
                    return ArtistSearchResponseDto.createResponseDto(artist, isLiked);
                })
                .collect(Collectors.toList());

        return artistSearchResponseList;
    }

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
    public List<LikeArtistResponseDto> getLikeArtistList() {
        // 로그인한 유저가 좋아하는 artist의 id 찾아오기
        // 임시로 id = 1 인 유저 호출
        User loginUser = userRepository.findById(1);

        // LikeList 엔티티 목록을 가져옴
        List<LikeList> likeListEntities = likeListRepository.findByUser_Id(loginUser.getId());

        // LikeList 엔티티에서 Artist 엔티티 추출
        List<Artist> likeArtistEntities = likeListEntities.stream()
                .map(LikeList::getArtist)
                .collect(Collectors.toList());

        List<LikeArtistResponseDto> likeArtistList = likeArtistEntities.stream()
                .map(artist -> LikeArtistResponseDto.createResponseDto(artist))
                .collect(Collectors.toList());

        return likeArtistList;
    }

    @Override
    public Map<String, Object> getLikeArtist(int artistId) {
        Map<String, Object> isLike = new HashMap<>();
        // 로그인한 유저 불러오기
        User loginUser = userRepository.findById(1);
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_ARTIST_EXCEPTION));

        // existsByUserAndArtist를 사용하여 존재 여부 확인
        boolean isLiked = likeListRepository.existsByUserAndArtist(loginUser, artist);
        System.out.println(isLiked);
        isLike.put("like", isLiked);

        return isLike;
    }

    @Override
    public void like(int artistId) {
        // 로그인한 유저 불러오기
        User loginUser = userRepository.findById(1);
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_ARTIST_EXCEPTION));

        // 현재 사용자가 이미 찜한 상태인지 확인
        Optional<LikeList> existingLike = likeListRepository.findByUserAndArtist(loginUser, artist);

        if (existingLike.isPresent()) {
            // 이미 찜한 상태라면 해제 (삭제)
            likeListRepository.delete(existingLike.get());
        } else {
            // 찜한 상태가 아니라면 찜하기 (저장)
            LikeList likeArtist = new LikeList(loginUser, artist);
            likeListRepository.save(likeArtist);
        }
    }
}
