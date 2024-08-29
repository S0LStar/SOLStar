package com.common.solstar.domain.artist.model.service;

import com.common.solstar.domain.artist.dto.response.ArtistResponseDto;
import com.common.solstar.domain.artist.dto.response.ArtistSearchResponseDto;
import com.common.solstar.domain.artist.dto.response.LikeArtistResponseDto;

import java.util.List;
import java.util.Map;

public interface ArtistService {

    List<ArtistSearchResponseDto> searchArtists(String keyword, String authEmail);

    ArtistResponseDto getArtistById(int artistId);

    List<LikeArtistResponseDto> getLikeArtistList(String authEmail);

    Map<String, Object> getLikeArtist(int artistId, String authEmail);

    void like(int artistId, String authEmail);
}
