package com.example.team9_SpringSecurity.util.ApiResponse;

public class ApiUtil {

    // 성공 응답 (반환 데이터 존재)
    public static ApiResult successResponse(CodeSuccess codeSuccess, Object response){
        return new ApiResult(codeSuccess, response, null);
    }

    // 에러 응답
    public static ApiResult errorResponse(CodeError codeError){
        return new ApiResult(null, null, codeError);
    }
}


