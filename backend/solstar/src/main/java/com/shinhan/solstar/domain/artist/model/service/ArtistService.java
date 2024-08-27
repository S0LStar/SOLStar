package com.shinhan.solstar.domain.artist.model.service;

import com.shinhan.solstar.domain.artist.dto.response.ArtistResponseDto;
import com.shinhan.solstar.domain.artist.dto.response.LikeArtistResponseDto;

import java.util.List;
import java.util.Map;

public interface ArtistService {

    ArtistResponseDto getArtistById(int artistId);

    List<LikeArtistResponseDto> getLikeArtistList();

    Map<String, Object> getLikeArtist(int artistId);

    void like(int artistId);
}
