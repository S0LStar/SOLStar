package com.shinhan.solstar.domain.artist.controller;

import com.shinhan.solstar.domain.artist.dto.response.ArtistResponseDto;
import com.shinhan.solstar.domain.artist.dto.response.LikeArtistResponseDto;
import com.shinhan.solstar.domain.artist.model.service.ArtistService;
import com.shinhan.solstar.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @GetMapping("/like")
    public ResponseEntity<?> getLikeArtist() {
        List<LikeArtistResponseDto> likeArtistList = artistService.getLikeArtistList();

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("찜한 아티스트 리스트 조회 완료")
                .data(likeArtistList)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 찜한 아티스트 조회
    @GetMapping("/like/{artistId}")
    public ResponseEntity<?> likeArtist(@PathVariable("artistId") int artistId) {
        Map<String, Object> isLike = artistService.getLikeArtist(artistId);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("찜한 아티스트 조회 완료")
                .data(isLike)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 아티스트 찜하기 버튼
    @PostMapping("/like/{artistId}")
    public ResponseEntity<?> like(@PathVariable("artistId") int artistId) {
        artistService.like(artistId);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("찜하기 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
