package com.common.solstar.domain.wallet.controller;

import com.common.solstar.domain.auth.model.service.AuthService;
import com.common.solstar.domain.wallet.dto.response.FindMyAccountReponse;
import com.common.solstar.domain.wallet.dto.response.FindMyHostFundingAccountResponse;
import com.common.solstar.domain.wallet.dto.response.FundingTransactionHistoryResponse;
import com.common.solstar.domain.wallet.model.service.WalletService;
import com.common.solstar.global.util.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet")
@Tag(name = "WalletController")
public class WalletController {

    private final WalletService walletService;

    @Operation(summary = "나의 연동 계좌 조회")
    @GetMapping("/my")
    public ResponseEntity<ResponseDto<FindMyAccountReponse>> getMyWallet(@RequestHeader(value = "Authorization", required = false) String header){

       FindMyAccountReponse result = walletService.getMyWallet(header);
       ResponseDto<FindMyAccountReponse> responseDto = ResponseDto.<FindMyAccountReponse>builder()
               .status(HttpStatus.OK.value())
               .message("나의 연동 계좌 정보 조회 성공")
               .data(result)
               .build();
       return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "나의 주최 펀딩 계좌 조회")
    @GetMapping("/my/host-funding")
    public ResponseEntity<ResponseDto<List<FindMyHostFundingAccountResponse>>> getMyHostFundingAccount(@RequestHeader(value = "Authorization", required = false) String header){
        List<FindMyHostFundingAccountResponse> result = walletService.getMyHostFundingAccount(header);
        ResponseDto<List<FindMyHostFundingAccountResponse>> responseDto = ResponseDto.<List<FindMyHostFundingAccountResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("내가 주최하는 펀딩의 계좌 조회 성공")
                .data(result)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "특정 펀딩의 계좌 거래 내역 조회")
    @PostMapping("/funding/{fundingId}")
    public ResponseEntity<ResponseDto<List<FundingTransactionHistoryResponse>>> getFundingTransactionHistory(@RequestHeader(value = "Authorization", required = false) String header,
                                                                                                             @PathVariable("fundingId") int fundingId){
        List<FundingTransactionHistoryResponse> result = walletService.getFundingTransactionHistory(header, fundingId);
        ResponseDto<List<FundingTransactionHistoryResponse>> responseDto = ResponseDto.<List<FundingTransactionHistoryResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("해당 펀딩의 계좌 거래 내역 조회")
                .data(result)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
