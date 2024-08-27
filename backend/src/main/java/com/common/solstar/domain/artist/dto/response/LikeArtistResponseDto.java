package com.common.solstar.domain.artist.dto.response;

import com.common.solstar.domain.artist.entity.Artist;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LikeArtistResponseDto {

    private int id;

    private String type;

    private String name;

    private String group;

    private String profileImage;

    @Builder
    private LikeArtistResponseDto(int id, String type, String name, String group, String profileImage) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.group = group;
        this.profileImage = profileImage;
    }

    public static LikeArtistResponseDto createResponseDto(Artist artist) {
        return LikeArtistResponseDto.builder()
                .id(artist.getId())
                .type(artist.getType().name())
                .name(artist.getName())
                .group(artist.getGroup())
                .profileImage(artist.getProfileImage())
                .build();
    }

}
