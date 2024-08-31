package com.common.solstar.domain.user.model.service;

import com.common.solstar.domain.agency.model.repository.AgencyRepository;
import com.common.solstar.domain.artist.model.repository.ArtistRepository;
import com.common.solstar.domain.funding.dto.response.FundingResponseDto;
import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.model.repository.FundingRepository;
import com.common.solstar.domain.fundingJoin.entity.FundingJoin;
import com.common.solstar.domain.fundingJoin.model.repository.FundingJoinRepository;
import com.common.solstar.domain.user.dto.request.UpdateIntroductionRequest;
import com.common.solstar.domain.user.dto.request.UpdateNicknameRequest;
import com.common.solstar.domain.user.dto.request.UpdateProfileImageRequest;
import com.common.solstar.domain.user.dto.response.UserDetailResponse;
import com.common.solstar.domain.user.entity.User;
import com.common.solstar.domain.user.model.repository.UserRepository;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FundingJoinRepository fundingJoinRepository;
    private final FundingRepository fundingRepository;
    private final AgencyRepository agencyRepository;
    private final ArtistRepository artistRepository;

    // 로그인 유저 정보 조회
    public UserDetailResponse getLoginUserDetail(String authEmail) {

        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        return UserDetailResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .introduction(user.getIntroduction())
                .email(user.getEmail())
                .birthday(user.getBirthDate())
                .phone(user.getPhone())
                .build();
    }

    // 참여 펀딩 조회
    public List<FundingResponseDto> getUserJoinFunding(String authEmail) {
        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        List<FundingJoin> fundingJoinList = fundingJoinRepository.findByUserId(user.getId());

        List<Funding> fundingList = fundingJoinList.stream()
                .map(FundingJoin::getFunding)
                .collect(Collectors.toList());

        List<FundingResponseDto> responseList = fundingList.stream()
                .map(funding -> FundingResponseDto.createResponseDto(funding))
                .collect(Collectors.toList());

        return responseList;
    }

    // 주최 펀딩 조회
    public List<FundingResponseDto> getHostFunding(String authEmail) {
        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        List<Funding> fundingList = fundingRepository.findByHostId(user.getId());

        List<FundingResponseDto> responseList = fundingList.stream()
                .map(funding -> FundingResponseDto.createResponseDto(funding))
                .collect(Collectors.toList());

        return responseList;

    }

    // 자기소개 수정
    @Transactional
    public void updateIntroduction(String authEmail, UpdateIntroductionRequest request) {
        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        if(request.getIntroduction() == null) {
            user.deleteIntroduction();
        }
        else {
            user.updateIntroduction(request.getIntroduction());
        }

        userRepository.save(user);
    }

    // 로그아웃
    public void logout(String authEmail) {

        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        user.deleteRefreshToken();
        userRepository.save(user);
    }

    // 특정 유저가 주최한 펀딩 조회
    public List<FundingResponseDto> getHostFundingById(String authEmail, int userId) {

        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        List<Funding> fundingList = fundingRepository.findByHostId(userId);

        List<FundingResponseDto> responseList = fundingList.stream()
                .map(funding -> FundingResponseDto.createResponseDto(funding))
                .collect(Collectors.toList());

        return responseList;

    }

    // 프로필 이미지 수정
    @Transactional
    public void updateProfileImage(String authEmail, UpdateProfileImageRequest request) {

        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        if(request.getProfileImage() == null) {
            user.deleteProfileImage();
        }
        else user.updateProfileImage(request.getProfileImage());
        userRepository.save(user);
    }

    // 닉네임 수정
    public void updateNickname(String authEmail, UpdateNicknameRequest request) {

        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        String nickname = request.getNickname();

        // 유저, 소속사, 아티스트와 겹치는 닉네임 불가능
        if(userRepository.existsByNickname(nickname) || agencyRepository.existsByName(nickname) || artistRepository.existsByName(nickname)){
            throw new ExceptionResponse(CustomException.DUPLICATED_NAME_EXCEPTION);
        }

        user.updateNickname(nickname);
        userRepository.save(user);

    }
}
