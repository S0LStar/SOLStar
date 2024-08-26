package com.shinhan.solstar.domain.artist.model.service;

import com.shinhan.solstar.domain.artist.dto.response.ArtistResponseDto;
import com.shinhan.solstar.domain.artist.entity.Artist;
import com.shinhan.solstar.domain.artist.model.repository.ArtistRepository;
import com.shinhan.solstar.domain.funding.dto.response.FundingDetailResponseDto;
import com.shinhan.solstar.domain.funding.dto.response.FundingResponseDto;
import com.shinhan.solstar.domain.funding.entity.Funding;
import com.shinhan.solstar.domain.funding.model.repository.FundingRepository;
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
}
