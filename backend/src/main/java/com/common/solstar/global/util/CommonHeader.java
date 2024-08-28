package com.common.solstar.global.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
public class CommonHeader {

    private String apiName;
    private String transmissionDate;
    private String transmissionTime;
    private String institutionCode;
    private String fintechAppNo;
    private String apiServiceCode;
    private String institutionTransactionUniqueNo;
    private String userKey;

    @Value("${ssafy.api.key}")
    private String apiKey;

    public CommonHeader() {

        String stringDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));

        this.transmissionDate = stringDate;
        this.transmissionTime = stringTime;
        this.institutionCode = "00100";
        this.fintechAppNo = "001";
        this.institutionTransactionUniqueNo = stringDate + stringTime + "000000";
    }

}
