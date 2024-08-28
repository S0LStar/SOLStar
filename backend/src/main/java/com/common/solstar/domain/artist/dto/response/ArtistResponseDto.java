package com.common.solstar.domain.artist.dto.response;

import com.common.solstar.domain.artist.entity.Artist;
import com.common.solstar.domain.funding.dto.response.FundingResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ArtistResponseDto {

    private String type;

    private String group;

    private String name;

    private String profileImage;

    private List<FundingResponseDto> fundingList;

    @Builder
    private ArtistResponseDto(String type, String group, String name, String profileImage, List<FundingResponseDto> fundingList) {
        this.type = type;
        this.group = group;
        this.name = name;
        this.profileImage = profileImage;
        this.fundingList = fundingList;
    }

    public static ArtistResponseDto createResponseDto(Artist artist, List<FundingResponseDto> fundingList) {
        return ArtistResponseDto.builder()
                .type(artist.getType().name())
                .group(artist.getGroup())
                .name(artist.getName())
                .profileImage(artist.getProfileImage())
                .fundingList(fundingList)
                .build();
    }

}
