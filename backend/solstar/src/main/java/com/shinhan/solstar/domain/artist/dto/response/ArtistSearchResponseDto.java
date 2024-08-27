package com.shinhan.solstar.domain.artist.dto.response;

import com.shinhan.solstar.domain.artist.entity.Artist;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistSearchResponseDto {

    private int id;

    private String type;

    private String name;

    private String group;

    private String profileImage;

    private int popularity;

    private boolean isLiked;

    public static ArtistSearchResponseDto createResponseDto(Artist artist, boolean isLiked) {
        return ArtistSearchResponseDto.builder()
                .id(artist.getId())
                .type(artist.getType().name())
                .name(artist.getName())
                .group(artist.getGroup())
                .profileImage(artist.getProfileImage())
                .popularity(artist.getPopularity())
                .isLiked(isLiked)
                .build();
    }

}
