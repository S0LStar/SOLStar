package com.shinhan.solstar.domain.artist.controller;

import com.shinhan.solstar.domain.artist.dto.response.ArtistResponseDto;
import com.shinhan.solstar.domain.artist.entity.Artist;
import com.shinhan.solstar.domain.artist.model.service.ArtistService;
import com.shinhan.solstar.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    // 검색어로 아티스트 조회

    // 특정 아티스트 상세 조회
    @GetMapping("/{artistId}")
    public ResponseEntity<?> getArtistById(@PathVariable("artistId") int artistId) {
        ArtistResponseDto artist = artistService.getArtistById(artistId);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("특정 아티스트 상세 조회 완료")
                .data(artist)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 찜한 아티스트 리스트 조회

    // 아티스트 찜하기

    // 아티스트 찜 해제하기
}
