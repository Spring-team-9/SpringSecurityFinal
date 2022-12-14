package com.example.team9_SpringSecurity.controller;

import com.example.team9_SpringSecurity.dto.LoginRequestDto;
import com.example.team9_SpringSecurity.dto.MessageDto;
import com.example.team9_SpringSecurity.dto.SignupRequestDto;
import com.example.team9_SpringSecurity.service.UserService;
import com.example.team9_SpringSecurity.util.ApiResponse.ApiResult;
import com.example.team9_SpringSecurity.util.ApiResponse.ApiUtil;
import com.example.team9_SpringSecurity.util.ApiResponse.CodeSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")

    public ApiResult signup(@RequestBody @Valid SignupRequestDto dto){
        MessageDto messageDto = userService.signup(dto);
        return ApiUtil.successResponse(CodeSuccess.JOIN_OK, messageDto);
    }

    @PostMapping("/login")
    public ApiResult login(@RequestBody LoginRequestDto dto, HttpServletResponse response){
        MessageDto messageDto = userService.login(dto, response);
        return ApiUtil.successResponse(CodeSuccess.LOGIN_OK, messageDto);
        
        
    @DeleteMapping("/login")
    public ApiResult deleteAccount(@RequestBody @Valid LoginRequestDto dto, HttpServletRequest request) {
        MessageDto messageDto = userService.delete(dto, request);
        return ApiUtil.successResponse(CodeSuccess.OK, messageDto);
    }
}
