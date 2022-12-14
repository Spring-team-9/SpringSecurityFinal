package com.example.team9_SpringSecurity.dto;

import com.example.team9_SpringSecurity.entity.User;
import com.example.team9_SpringSecurity.util.ApiResponse.CodeSuccess;
import lombok.Getter;

@Getter
public class UserDto extends MessageDto{
    private User user;

    public UserDto(CodeSuccess status, User user) {
        super(status);
        this.user = user;
    }
}
