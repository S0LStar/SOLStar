package com.common.solstar.domain.artist.controller;

import com.common.solstar.domain.artist.dto.response.ArtistResponseDto;
import com.common.solstar.domain.artist.dto.response.ArtistSearchResponseDto;
import com.common.solstar.domain.artist.dto.response.LikeArtistResponseDto;
import com.common.solstar.domain.artist.model.service.ArtistService;
import com.common.solstar.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/artist")
@RequiredArgsConstructor
@Tag(name = "ArtistController")
public class ArtistController {

    private final ArtistService artistService;

    // 검색어로 아티스트 조회
    @GetMapping
    @Operation(summary = "아티스트 검색 조회")
    public ResponseEntity<?> searchArtists(@RequestParam("keyword") String keyword) {
        List<ArtistSearchResponseDto> artistList = artistService.searchArtists(keyword);

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("아티스트 검색 완료")
                .data(Map.of("artistList", artistList))
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 특정 아티스트 상세 조회
    @GetMapping("/{artistId}")
    @Operation(summary = "특정 아티스트 상세 조회")
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
    @Operation(summary = "찜한 아티스트 전체 조회")
    public ResponseEntity<?> getLikeArtist() {
        List<LikeArtistResponseDto> likeArtistList = artistService.getLikeArtistList();

        ResponseDto<Object> responseDto = ResponseDto.<Object>builder()
                .status(HttpStatus.OK.value())
                .message("찜한 아티스트 리스트 조회 완료")
                .data(Map.of("likeArtistList", likeArtistList))
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 찜한 아티스트 조회
    @GetMapping("/like/{artistId}")
    @Operation(summary = "찜한 아티스트 조회")
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
    @Operation(summary = "아티스트 찜하기 / 취소하기")
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
