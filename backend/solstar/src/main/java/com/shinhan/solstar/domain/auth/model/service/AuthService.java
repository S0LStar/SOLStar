package com.shinhan.solstar.domain.auth.model.service;

import com.shinhan.solstar.domain.account.entity.Account;
import com.shinhan.solstar.domain.account.model.repository.AccountRepository;
import com.shinhan.solstar.domain.agency.model.repository.AgencyRepository;
import com.shinhan.solstar.domain.auth.dto.request.SignupRequest;
import com.shinhan.solstar.domain.user.entity.User;
import com.shinhan.solstar.domain.user.model.repository.UserRepository;
import com.shinhan.solstar.global.exception.CustomException;
import com.shinhan.solstar.global.exception.ExceptionResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;
    private final AccountRepository accountRepository;


    // 회원가입
    @Transactional
    public void signup(SignupRequest signupRequest) {

        String email = signupRequest.getEmail();
        String nickname = signupRequest.getNickname();

        // 이미 가입된 이메일이라면 가입 불가능
        if(userRepository.existsByEmail(email) || agencyRepository.existsByEmail(email)) {
            throw new ExceptionResponse(CustomException.DUPLICATED_ID_EXCEPTION);
        }

        // 사용 불가능한 닉네임일 경우 (유저, 소속사에서 겹치는거 제외. 아티스트 이름이랑도 겹치는거 추가 구현 예정)
        if(userRepository.existsByNickname(nickname) || agencyRepository.existsByName(nickname)){
            throw new ExceptionResponse(CustomException.DUPLICATED_NAME_EXCEPTION);
        }

        String userKey = "userKey"; // 금융 api에서 email로 계좌 정보 조회에서 불러와야함

        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .name(signupRequest.getName())
                .birthDate(signupRequest.getBirthDate())
                .phone(signupRequest.getPhone())
                .profileImage(signupRequest.getProfileImage())
                .userKey(userKey) // userKey 받아오는 로직 수정필요
                .build();

        Account account = Account.builder()
                .user(user)
                .accountNumber(signupRequest.getAccountNumber())
                .password(signupRequest.getAccountPassword())
                .bankCode(signupRequest.getBankCode())
                .build();

        userRepository.save(user);
        accountRepository.save(account);

    }

}
