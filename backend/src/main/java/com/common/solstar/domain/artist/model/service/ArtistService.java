package com.common.solstar.domain.artist.model.service;

import com.common.solstar.domain.artist.dto.response.ArtistResponseDto;
import com.common.solstar.domain.artist.dto.response.ArtistSearchResponseDto;
import com.common.solstar.domain.artist.dto.response.LikeArtistResponseDto;

import java.util.List;
import java.util.Map;

public interface ArtistService {

    List<ArtistSearchResponseDto> searchArtists(String keyword);

    ArtistResponseDto getArtistById(int artistId);

    List<LikeArtistResponseDto> getLikeArtistList();

    Map<String, Object> getLikeArtist(int artistId);

    void like(int artistId);
}
