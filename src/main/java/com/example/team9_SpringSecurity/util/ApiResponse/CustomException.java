package com.example.team9_SpringSecurity.util.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 전역으로 사용할 CustomException클래스
public class CustomException extends RuntimeException {     //실행 예외 클래스를 상속받아서 Unchecked Exception으로 활용
    private final CodeError codeError;

}
