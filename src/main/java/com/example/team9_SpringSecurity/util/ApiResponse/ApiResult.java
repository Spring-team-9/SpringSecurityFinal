package com.example.team9_SpringSecurity.util.ApiResponse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiResult {
    private final LocalDateTime timestamp =  LocalDateTime.now();
    private final int status;
    private final String message;
    private final Object response;
    private final CodeError error;
    public ApiResult(CodeSuccess codeSuccess, Object response, CodeError error){

        // 성공 코드 존재시 성공코드를 대입, 부존재시 에러코드를 대입
        if (codeSuccess != null){
            this.status = codeSuccess.getHttpStatus().value();
            this.message = codeSuccess.getDetail();
        } else {
            this.status = error.getHttpStatus().value();
            this.message = error.getDetail();
        }
        this.response = response;
        this.error = error;
    }
}
