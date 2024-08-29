package com.common.solstar.domain.user.model.service;

import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.funding.model.repository.FundingRepositorySupport;
import com.common.solstar.domain.fundingJoin.model.repository.FundingJoinRepository;
import com.common.solstar.domain.fundingJoin.model.repository.FundingJoinRepositorySupport;
import com.common.solstar.domain.user.dto.request.UpdateIntroductionRequest;
import com.common.solstar.domain.user.dto.response.HostFundingResponse;
import com.common.solstar.domain.user.dto.response.UserDetailResponse;
import com.common.solstar.domain.user.dto.response.UserJoinFundingReponse;
import com.common.solstar.domain.user.entity.User;
import com.common.solstar.domain.user.model.repository.UserRepository;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FundingJoinRepository fundingJoinRepository;
    private final FundingJoinRepositorySupport fundingJoinRepositorySupport;
    private final FundingRepositorySupport fundingRepositorySupport;

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
                .build();
    }

    // 참여 펀딩 조회
    public List<UserJoinFundingReponse> getUserJoinFunding(String authEmail) {
        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));

        return fundingJoinRepositorySupport.findFundingByUserEmail(user.getId());
    }

    // 주최 펀딩 조회
    public List<HostFundingResponse> getHostFunding(String authEmail) {
        if(authEmail == null) {
            throw new ExceptionResponse(CustomException.ACCESS_DENIEND_EXCEPTION);
        }

        User user = userRepository.findByEmail(authEmail)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));
        return fundingRepositorySupport.findFundingByHostId(user.getId());

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
}
