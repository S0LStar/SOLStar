package com.shinhan.solstar.domain.artist.model.service;

import com.shinhan.solstar.domain.artist.dto.response.ArtistResponseDto;
import com.shinhan.solstar.domain.artist.dto.response.LikeArtistResponseDto;

import java.util.List;

public interface ArtistService {

    ArtistResponseDto getArtistById(int artistId);

    List<LikeArtistResponseDto> getLikeArtist();

    void like(int artistId);
}
