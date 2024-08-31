package com.common.solstar.global.api.exception;

import com.common.solstar.global.exception.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.common.solstar.global.exception.CustomException;
import org.springframework.stereotype.Component;

@Component
public class WebClientExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handleWebClientException(String errorBody) throws ExceptionResponse {
        try {
            JsonNode root = objectMapper.readTree(errorBody);

            if (root.has("responseCode")) {
                String responseCode = root.get("responseCode").asText();

                switch (responseCode) {
                    // H1000 - H1009
                    case "H1000":
                        throw new ExceptionResponse(CustomException.INVALID_HEADER);
                    case "H1001":
                        throw new ExceptionResponse(CustomException.INVALID_API_NAME);
                    case "H1002":
                        throw new ExceptionResponse(CustomException.INVALID_TRANSMISSION_DATE);
                    case "H1003":
                        throw new ExceptionResponse(CustomException.INVALID_TRANSMISSION_TIME);
                    case "H1004":
                        throw new ExceptionResponse(CustomException.INVALID_INSTITUTION_CODE);
                    case "H1005":
                        throw new ExceptionResponse(CustomException.INVALID_FINTECH_APP_NO);
                    case "H1006":
                        throw new ExceptionResponse(CustomException.INVALID_API_SERVICE_CODE);
                    case "H1007":
                        throw new ExceptionResponse(CustomException.DUPLICATE_INSTITUTION_TRANSACTION_UNIQUE_NO);
                    case "H1008":
                        throw new ExceptionResponse(CustomException.INVALID_API_KEY);
                    case "H1009":
                        throw new ExceptionResponse(CustomException.INVALID_USER_KEY);
                    case "H1010":
                        throw new ExceptionResponse(CustomException.INVALID_INSTITUTION_TRANSACTION_UNIQUE_NO);

                    case "E4001":
                        throw new ExceptionResponse(CustomException.EMPTY_OR_INVALID_DATA);
                    case "E4003":
                        throw new ExceptionResponse(CustomException.NOT_FOUND_ID);
                    case "E4004":
                        throw new ExceptionResponse(CustomException.NOT_FOUND_API_KEY);

                        // A1001 - A1090
                    case "A1001":
                        throw new ExceptionResponse(CustomException.INVALID_BANK_CODE);
                    case "A1003":
                        throw new ExceptionResponse(CustomException.INVALID_ACCOUNT_NO);
                    case "A1004":
                        throw new ExceptionResponse(CustomException.INVALID_START_DATE);
                    case "A1005":
                        throw new ExceptionResponse(CustomException.INVALID_END_DATE);
                    case "A1006":
                        throw new ExceptionResponse(CustomException.INVALID_TRANSACTION_TYPE);
                    case "A1007":
                        throw new ExceptionResponse(CustomException.INVALID_ORDER_BY_TYPE);
                    case "A1011":
                        throw new ExceptionResponse(CustomException.INVALID_TRANSACTION_BALANCE);
                    case "A1014":
                        throw new ExceptionResponse(CustomException.INSUFFICIENT_ACCOUNT_BALANCE);
                    case "A1016":
                        throw new ExceptionResponse(CustomException.TRANSFER_LIMIT_EXCEEDED_ONCE);
                    case "A1017":
                        throw new ExceptionResponse(CustomException.TRANSFER_LIMIT_EXCEEDED_DAILY);
                    case "A1018":
                        throw new ExceptionResponse(CustomException.TRANSACTION_SUMMARY_LENGTH_EXCEEDED);
                    case "A1019":
                        throw new ExceptionResponse(CustomException.NOT_FOUND_ACCOUNT_TYPE_UNIQUE_NO);
                    case "A1023":
                        throw new ExceptionResponse(CustomException.INVALID_ACCOUNT_TYPE_UNIQUE_NO);
                    case "A1086":
                        throw new ExceptionResponse(CustomException.NOT_FOUND_AUTH_CODE_RECORD);
                    case "A1087":
                        throw new ExceptionResponse(CustomException.AUTHENTICATION_TIME_EXPIRED);
                    case "A1088":
                        throw new ExceptionResponse(CustomException.AUTH_CODE_MISMATCH);
                    case "A1089":
                        throw new ExceptionResponse(CustomException.INVALID_AUTH_TEXT);
                    case "A1090":
                        throw new ExceptionResponse(CustomException.INVALID_AUTH_CODE);
                    case "A5004":
                        throw new ExceptionResponse(CustomException.KRW_ACCOUNT_ONLY);

                        // Q1000 - Q1001
                    case "Q1000":
                        throw new ExceptionResponse(CustomException.ETC_EXCEPTION);
                    case "Q1001":
                        throw new ExceptionResponse(CustomException.INVALID_REQUEST_BODY_FORMAT_EXCEPTION);

                    default:
                        throw new ExceptionResponse(CustomException.ETC_EXCEPTION); // 기본 예외 처리
                }
            } else {
                throw new ExceptionResponse(CustomException.BAD_SSAFY_API_REQUEST);
            }

        } catch (JsonMappingException e) {
            throw new RuntimeException("JSON 맵핑 중 오류가 발생했습니다.", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        }
    }

}
