package com.common.solstar.domain.artist.controller;

import com.common.solstar.domain.artist.dto.response.ArtistResponseDto;
import com.common.solstar.domain.artist.dto.response.ArtistSearchResponseDto;
import com.common.solstar.domain.artist.dto.response.LikeArtistResponseDto;
import com.common.solstar.domain.artist.entity.Artist;
import com.common.solstar.domain.artist.model.repository.ArtistRepository;
import com.common.solstar.domain.artist.model.service.ArtistService;
import com.common.solstar.global.auth.jwt.JwtUtil;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.common.solstar.global.util.ImageUtil;
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
    private final JwtUtil jwtUtil;
    private final ArtistRepository artistRepository;
    private final ImageUtil imageUtil;

    // 검색어로 아티스트 조회
    @GetMapping
    @Operation(summary = "아티스트 검색 조회")
    public ResponseEntity<?> searchArtists(@RequestHeader(value = "Authorization", required = false) String header,
                                           @RequestParam("keyword") String keyword) {
        String authEmail = jwtUtil.getLoginUser(header);
        List<ArtistSearchResponseDto> artistList = artistService.searchArtists(keyword, authEmail);

        Artist artist = artistRepository.findById(1)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_ARTIST_EXCEPTION));

        String imageUrl = artist.getProfileImage();

        String fileName = imageUtil.extractFileName(imageUrl);

        System.out.println(fileName);

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
    public ResponseEntity<?> getLikeArtist(@RequestHeader(value = "Authorization", required = false) String header) {
        String authEmail = jwtUtil.getLoginUser(header);
        List<LikeArtistResponseDto> likeArtistList = artistService.getLikeArtistList(authEmail);

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
    public ResponseEntity<?> likeArtist(@RequestHeader(value = "Authorization", required = false) String header,
                                        @PathVariable("artistId") int artistId) {
        String authEmail = jwtUtil.getLoginUser(header);
        Map<String, Object> isLike = artistService.getLikeArtist(artistId, authEmail);

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
    public ResponseEntity<?> like(@RequestHeader(value = "Authorization", required = false) String header,
                                  @PathVariable("artistId") int artistId) {
        String authEmail = jwtUtil.getLoginUser(header);
        artistService.like(artistId,authEmail);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("찜하기 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
