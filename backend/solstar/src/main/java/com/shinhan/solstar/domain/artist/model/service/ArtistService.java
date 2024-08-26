package com.shinhan.solstar.domain.artist.model.service;

import com.shinhan.solstar.domain.artist.dto.response.ArtistResponseDto;

public interface ArtistService {

    ArtistResponseDto getArtistById(int artistId);
}
