package com.common.solstar.domain.artist.dto.response;

import com.common.solstar.domain.artist.entity.Artist;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeArtistResponseDto {

    private int artistId;

    private String type;

    private String name;

    private String group;

    private String profileImage;

    public static LikeArtistResponseDto createResponseDto(Artist artist) {
        return LikeArtistResponseDto.builder()
                .artistId(artist.getId())
                .type(artist.getType().name())
                .name(artist.getName())
                .group(artist.getGroup())
                .profileImage(artist.getProfileImage())
                .build();
    }

}
