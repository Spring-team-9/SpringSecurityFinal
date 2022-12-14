package com.example.assignment_memo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

// Response로 나갈 모든 데이터 형식을 하나로 받기 위한 Dto
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // null값 아닌 것만 Json에 추가하기
public class MessageDto<T> {
    private T data;

    public MessageDto(){
    }

    public MessageDto(StatusEnum status){
    }

    public MessageDto(StatusEnum status, T dto){
        this.data = dto;
    }
}
