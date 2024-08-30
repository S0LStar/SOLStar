package com.common.solstar.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomException {

    // 400
    NOT_EMPTY_ROLE_EXCEPTION(400,"NotEmptyRoleException","권한이 존재하지 않습니다."),
    NOT_FOUND_USER_EXCEPTION(400,"NotFoundUserException","유저가 존재하지 않습니다."),
    PASSWORD_INPUT_EXCEPTION(400,"PasswordInputException","비밀번호 입력이 잘못 되었습니다."),
    ID_PASSWORD_INPUT_EXCEPTION(400,"IdPasswordInputException", "아이디 패스워드 입력이 잘못 되었습니다."),
    NOT_AUTH_NUMBER_EXCEPTION(400,"NotAuthNumberException","인증되지 않은 번호입니다."),
    DUPLICATED_NUMBER_EXCEPTION(400,"DuplicatedNumberException","가입된 전화번호가 존재합니다."),
    NOT_SAME_USER_EXCEPTION(400,"NotSameUserException","로그인 유저와 수정 회원이 일지하지 않습니다."),
    DUPLICATED_ID_EXCEPTION(400,"DuplicatedIDException","가입된 아이디가 존재합니다."),
    DUPLICATED_NAME_EXCEPTION(400,"DuplicateNameException","가입된 닉네임 또는 이름이 존재합니다."),


    // 잘못된 API 요청
    BAD_SSAFY_API_REQUEST(400,"BadSsafypiRequestException","잘못된 금융 API 요청입니다."),
    EXPIRED_AUTH_CODE(403,"ExpriedAuthCode", "인증시간이 만료되었습니다."),
    NOT_FOUND_AUTH_CODE(404,"NotFoundAuthCode","인증코드 발급 기록이 없습니다.(이미 인증한 코드입니다)"),
    MISMATCHED_AUTH_CODE_EXCEPTION(400,"MismatchedAuthCodeException","인증코드가 일치하지 않습니다."),
    INVALID_ACCOUNT_NUMBER_EXCEPTION(400, "InvalidAccountNumberException", "계좌번호가 유효하지 않습니다."),

    // 계좌
    NOT_FOUND_ACCOUNT_EXCEPTION(400, "NotFoundAccountException", "계좌가 존재하지 않습니다."),
    NOT_MATCH_ACCOUNT_PASSWORD_EXCEPTION(403, "NotMatchAccountPasswordException", "해당 계좌의 비밀번호가 맞지 않습니다."),

    // 펀딩
    NOT_FOUND_FUNDING_EXCEPTION(400, "NotFoundFundingException", "펀딩이 존재하지 않습니다"),
    NOT_PROCESSING_FUNDING_EXCEPTION(400, "NotProcessingFundingException", "현재 진행중인 펀딩이 아닙니다."),
    NOT_SUCCESS_FUNDING_EXCEPTION(400, "NotSuccessFundingException", "현재 완료된 펀딩이 아닙니다."),
    INVALID_FUNDING_JOIN_EXCEPTION(400, "InvalidFundingJoinException", "올바르지 않은 펀딩 참여 형식입니다."),
    INVALID_FUNDING_HOST_EXCEPTION(400, "InvalidFundingHostException", "해당 사용자는 펀딩 주최자가 아닙니다."),

    // 펀딩 공지사항
    NOT_FOUND_BOARD_EXCEPTION(400, "NotFoundBoardException", "펀딩 공지사항이 존재하지 않습니다."),

    // 펀딩 계좌
    TRANSFER_FAILURE_EXCEPTION(400, "TransferFailureException", "이체에 실패했습니다."),

    // 펀딩 소속사
    NOT_FOUND_FUNDING_AGENCY_EXCEPTION(400, "NotFoundFundingAgencyException", "해당 소속사와 연결된 펀딩이 아닙니다."),
    NOT_MATCH_AGENCY_EXCEPTION(400, "NotMatchAgencyException", "해당 소속사는 펀딩 소속사와 일치하지 않습니다."),
    ALREADY_ACCEPT_FUNDING_EXCEPTION(400, "AlreadyAcceptFundingException", "해당 펀딩은 이미 소속사 승인이 되었습니다."),

    // 아티스트
    NOT_FOUND_ARTIST_EXCEPTION(400, "NotFoundArtistException", "아티스트가 존재하지 않습니다."),

    // 소속사
    NOT_FOUND_AGENCY_EXCEPTION(400, "NotFoundAgencyException", "소속사가 존재하지 않습니다."),

    // 인증 에러 401
    EXPIRED_JWT_EXCEPTION(401,"ExpiredJwtException","토큰이 만료했습니다."),
    NOT_VALID_JWT_EXCEPTION(401,"NotValidJwtException","토큰이 유효하지 않습니다."),

    // 403
    ACCESS_DENIEND_EXCEPTION(403,"AccessDeniendException","권한이 없습니다"),

    // 금융 API 예외
    // H1000
    INVALID_HEADER(400,"InvalidHeaderException","HEADER 정보가 유효하지 않습니다."),
    // H1001
    INVALID_API_NAME(400,"InvalidApiNameException","API 이름이 유효하지 않습니다."),
    // H1002
    INVALID_TRANSMISSION_DATE(400,"InvalidTransmissionDate","전송일자 형식이 유효하지 않습니다."),
    // H1003
    INVALID_TRANSMISSION_TIME(400,"InvalidTransmissionTimeException","전송시간 형식이 유효하지 않습니다."),
    // H1004
    INVALID_INSTITUTION_CODE(400,"InvalidInstitutionCodeException","기관 코드가 유효하지 않습니다."),
    // H1005
    INVALID_FINTECH_APP_NO(400,"InvalidFintechAppNoException","핀테크 애플리케이션 번호가 유효하지 않습니다."),
    // H1006
    INVALID_API_SERVICE_CODE(400,"InvalidApiServiceCodeException","API 서비스 코드가 유효하지 않습니다."),
    // H1010
    INVALID_INSTITUTION_TRANSACTION_UNIQUE_NO(400,"InvalidInstitutionTransactionUniqueNoException","기관 거래 고유번호가 유효하지 않습니다."),
    // H1008
    INVALID_API_KEY(400,"InvalidApiKeyException","API 키가 유효하지 않습니다."),
    // H1009
    INVALID_USER_KEY(400,"InvalidUserKeyException","사용자 키가 유효하지 않습니다."),
    // H1007
    DUPLICATE_INSTITUTION_TRANSACTION_UNIQUE_NO(400,"DuplicateInstitutionTransactionUniqueNoException","기관 거래 고유번호가 중복된 값입니다."),


    // E4001
    EMPTY_OR_INVALID_DATA(400, "EmptyOrInvalidDataException", "빈 데이터이거나 형식에 맞지 않는 데이터입니다."),
    // E4003
    NOT_FOUND_ID(404, "NotFoundIdException", "존재하지 않는 ID입니다."),
    // E4004
    NOT_FOUND_API_KEY(404, "NotFoundApiKeyException", "존재하지 않는 API KEY입니다."),


    // A1001
    INVALID_BANK_CODE(400,"InvalidBankCodeException","은행코드가 유효하지 않습니다."),
    // A1003
    INVALID_ACCOUNT_NO(400, "InvalidAccountNoException","계좌번호가 유효하지 않습니다."),
    // A1004
    INVALID_START_DATE(400,"InvalidStartDate","조회 시작일자가 유효하지 않습니다."),
    // A1005
    INVALID_END_DATE(400,"InvalidEndDate","조회 종료일자가 유효하지 않습니다."),
    // A1006
    INVALID_TRANSACTION_TYPE(400,"InvalidTransactionType","거래 구분이 유효하지 않습니다"),
    // A1007
    INVALID_ORDER_BY_TYPE(400,"InvalidOrderByType","정렬 순서가 유효하지 않습니다."),
    // A1011
    INVALID_TRANSACTION_BALANCE(400,"InvalidTransactionBalance","거래금액이 유효하지 않습니다."),
    // A1014
    INSUFFICIENT_ACCOUNT_BALANCE(400, "InsufficientAccountBalanceException", "계좌 잔액이 부족하여 거래가 실패했습니다."),
    // A1016
    TRANSFER_LIMIT_EXCEEDED_ONCE(400, "TransferLimitExceededOnceException", "이체 가능 한도 초과(1회)."),
    // A1017
    TRANSFER_LIMIT_EXCEEDED_DAILY(400, "TransferLimitExceededDailyException", "이체 가능 한도 초과(1일)."),
    // A1018
    TRANSACTION_SUMMARY_LENGTH_EXCEEDED(400, "TransactionSummaryLengthExceededException", "거래요약내용 길이가 초과되었습니다."),

    // A1019
    NOT_FOUND_ACCOUNT_TYPE_UNIQUE_NO(404,"NotFoundAccountTypeUniqueNo","없는 상품입니다. 은행별 상품 조회를 다시 확인해주세요."),
    // A1023
    INVALID_ACCOUNT_TYPE_UNIQUE_NO(400,"InvalidAccountTypeUniqueNo","상품고유번호가 유효하지 않습니다."),
    // A1086
    NOT_FOUND_AUTH_CODE_RECORD(404,"NotFoundAuthCodeRecord","인증코드 발급 기록이 없습니다."),
    // A1087
    AUTHENTICATION_TIME_EXPIRED(400, "AuthenticationTimeExpiredException", "인증시간이 만료되었습니다."),
    // A1088
    AUTH_CODE_MISMATCH(400, "AuthCodeMismatchException", "인증코드가 일치하지 않습니다."),
    // A1089
    INVALID_AUTH_TEXT(400,"InvalidAuthText","기업명이 유효하지 않습니다."),
    // A1090
    INVALID_AUTH_CODE(400, "InvalidAuthCodeException", "인증코드가 유효하지 않습니다."),
    // A5004
    KRW_ACCOUNT_ONLY(400, "KrwAccountOnlyException", "원화 계좌만 가능합니다."),


    // Q1000
    ETC_EXCEPTION(400,"EtcException", "그 이외에 에러 메시지"),
    // Q1001
    INVALID_REQUEST_BODY_FORMAT_EXCEPTION(400,"InvalidRequestBodyFormatException","요청 본문의 형식이 잘못되었습니다. JSON형식 또는 데이터 타입을 확인해 주세요.");


    ;

    private int statusNum;
    private String errorCode;
    private String errorMessage;

}
