package com.common.solstar.domain.auth.model.service;

import com.common.solstar.domain.account.entity.Account;
import com.common.solstar.domain.account.model.repository.AccountRepository;
import com.common.solstar.domain.agency.entity.Agency;
import com.common.solstar.domain.agency.model.repository.AgencyRepository;
import com.common.solstar.domain.auth.dto.request.*;
import com.common.solstar.domain.auth.dto.response.CheckAuthCodeResponse;
import com.common.solstar.domain.auth.dto.response.LoginResponse;
import com.common.solstar.domain.auth.dto.response.RefreshResponse;
import com.common.solstar.domain.auth.dto.response.UserAccountValidateResponse;
import com.common.solstar.domain.user.entity.User;
import com.common.solstar.domain.user.model.repository.UserRepository;
import com.common.solstar.global.api.request.CheckAuthCodeApiRequest;
import com.common.solstar.global.api.request.OpenAccountAuthApiRequest;
import com.common.solstar.global.auth.CustomUserDetailsService;
import com.common.solstar.global.auth.jwt.JwtUtil;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import com.common.solstar.global.api.request.CommonHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;
    private final AccountRepository accountRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://finopenapi.ssafy.io/ssafy/api/v1")
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ssafy.api.key}")
    private String apiKey;

    private final PasswordEncoder passwordEncoder;


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

        Account account = Account.builder()
                .accountNumber(signupRequest.getAccountNumber())
                .password(signupRequest.getAccountPassword())
                .bankCode(signupRequest.getBankCode())
                .build();

        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .password(bCryptPasswordEncoder.encode(signupRequest.getPassword()))
                .name(signupRequest.getName())
                .birthDate(signupRequest.getBirthDate())
                .phone(signupRequest.getPhone())
                .profileImage(signupRequest.getProfileImage())
                .account(account)
                .userKey(signupRequest.getUserKey()) // userKey 받아오는 로직 수정필요
                .build();

        userRepository.save(user);
        accountRepository.save(account);

    }

    // 로그인
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String role = loginRequest.getRole();

        // role에 따라 적절한 repository를 통해 사용자 정보 로드
        UserDetails userDetails = customUserDetailsService.loadUserByEmailAndRole(email, role);

        try {
            // 인증 절차
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(), password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 토큰 발급
            String accessToken = jwtUtil.generateAccessToken(email, role);
            String refreshToken = jwtUtil.generateRefreshToken(email, role);

            // 일반 유저일 경우
            if ("USER".equals(role)) {
                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) {
                    user.updateRefreshToken(refreshToken);
                }
            }

            // 소속사 유저일 경우
            else if ("AGENCY".equals(role)) {
                Agency agency = agencyRepository.findByEmail(email).orElse(null);
                if (agency != null) {
                    agency.updateRefreshToken(refreshToken);
                }
            }

            return new LoginResponse(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            // 예외 메시지 출력
            throw new ExceptionResponse(CustomException.PASSWORD_INPUT_EXCEPTION);
        }
    }


    // 토큰 재발급
    public RefreshResponse refresh(String refreshToken) {

        // 만료된 리프레시 토큰일 경우
        if(!jwtUtil.validateToken(refreshToken)) {
            throw new ExceptionResponse(CustomException.EXPIRED_JWT_EXCEPTION);
        }

        String role = jwtUtil.roleFromToken(refreshToken);
        String email = null;

        if(role.equals("USER")) {
            // 해당 리프레시 토큰을 갖는 유저가 없을 경우
            User user = userRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(()-> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));
            email = user.getEmail();
        }
        else if(role.equals("AGENCY")) {
            Agency agency = agencyRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));
            email = agency.getEmail();
        }

        RefreshResponse refreshResponse = RefreshResponse.builder()
                .accessToken(jwtUtil.generateAccessToken(email, role))
                .build();
        return refreshResponse;
    }

    // 금융 API 상 사용자 계정 조회 (useKey 조회용)
    public UserAccountValidateResponse validateUser(UserAccountValidateRequest request) {
        String url = "/member/search";

        String apiRequest = """
                {
                    "apiKey" : "%s",
                    "userId" : "%s"
                }
                """.formatted(apiKey, request.getUserId());

        // API 요청 보내기
        Mono<String> responseMono = webClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .bodyValue(apiRequest)
                .retrieve()
                .bodyToMono(String.class);

        String response = responseMono.block();

        try{
            JsonNode root = objectMapper.readTree(response);

            if(!root.has("userKey")) {
                throw new ExceptionResponse(CustomException.NOT_SAME_USER_EXCEPTION);
            }

            String userKey = root.get("userKey").asText();

            return UserAccountValidateResponse.builder()
                    .useKey(userKey)
                    .build();
        } catch (Exception e){
            throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
        }

    }

    // 1원 송금
    public void sendAccountAuth(OpenAccountAuthRequest request) {

        String url = "/edu/accountAuth/openAccountAuth";

        CommonHeader header = CommonHeader.builder()
                .apiName("openAccountAuth")
                .apiServiceCode("openAccountAuth")
                .userKey(request.getUserKey())
                .apiKey(apiKey)
                .build();
        header.setCommonHeader();

        OpenAccountAuthApiRequest apiRequest = OpenAccountAuthApiRequest.builder()
                .header(header)
                .accountNo(request.getAccountNo())
                .authText("SOLSTAR")
                .build();

        try {

            // API 요청 보내기
            Mono<String> responseMono = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .bodyValue(apiRequest)  // JSON 바디 직접 전송
                    .retrieve()
                    .bodyToMono(String.class);

            String response = responseMono.block();

            JsonNode root = objectMapper.readTree(response);
            if(root.has("responseCode")) {
                throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
            }
        } catch (Exception e) {
            throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
        }


    }

    //1원 송금 검증
    public CheckAuthCodeResponse checkAuthCode(CheckAuthCodeRequest request) {

        String url = "/edu/accountAuth/checkAuthCode";

        CommonHeader header = CommonHeader.builder()
                .apiName("checkAuthCode")
                .apiServiceCode("checkAuthCode")
                .userKey(request.getUserKey())
                .apiKey(apiKey)
                .build();
        header.setCommonHeader();

        CheckAuthCodeApiRequest apiRequest = CheckAuthCodeApiRequest.builder()
                .header(header)
                .accountNo(request.getAccountNo())
                .authText("SOLSTAR")
                .authCode(request.getAuthCode())
                .build();

        try {
            // API 요청 보내기
            Mono<String> responseMono = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .bodyValue(apiRequest)
                    .retrieve()
                    .bodyToMono(String.class);

            String response = responseMono.block();
            JsonNode root = objectMapper.readTree(response);

            // 성공 상태면 true 반환
            if (root.get("REC").get("status").asText().equals("SUCCESS")) {
                return CheckAuthCodeResponse.builder()
                        .isSuccess(true)
                        .build();
            }

            // 실패면 false 반환
            return CheckAuthCodeResponse.builder().isSuccess(false).build();

        } catch (WebClientResponseException e) {
            // WebClient 오류 응답 처리
            String errorBody = e.getResponseBodyAsString();
            System.out.println("Error Response: " + errorBody);

            try {
                // 오류 응답 JSON 파싱
                JsonNode root = objectMapper.readTree(errorBody);

                if (root.has("responseCode")) {
                    String responseCode = root.get("responseCode").asText();

                    // responseCode에 따른 커스텀 예외 처리
                    switch (responseCode) {
                        case "A1087":
                            throw new ExceptionResponse(CustomException.EXPIRED_AUTH_CODE);
                        default:
                            throw new ExceptionResponse(CustomException.NOT_FOUND_AUTH_CODE);
                    }
                } else {
                    throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
                }

            } catch (JsonProcessingException jsonParseException) {
                // JSON 파싱 오류 시 기본 예외 처리
                throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
            }
        } catch (Exception e) {
            // 기타 예외 처리
            throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
        }
    }

    // 소속사 회원가입
    @Transactional
    public void agencySignup(AgencySignupRequest request) {

        String email = request.getEmail();
        // 이미 가입된 이메일이라면 가입 불가능
        if(userRepository.existsByEmail(email) || agencyRepository.existsByEmail(email)) {
            throw new ExceptionResponse(CustomException.DUPLICATED_ID_EXCEPTION);
        }

        Agency agency = Agency.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .name(request.getName())
                .build();

        agencyRepository.save(agency);

    }
}
