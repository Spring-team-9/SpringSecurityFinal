package com.example.team9_SpringSecurity.controller;

import com.example.team9_SpringSecurity.dto.MemoRequestDto;
import com.example.team9_SpringSecurity.dto.MessageDto;
import com.example.team9_SpringSecurity.dto.ReplyRequestDto;

import com.example.team9_SpringSecurity.service.MemoService;
import com.example.team9_SpringSecurity.util.ApiResponse.ApiResult;
import com.example.team9_SpringSecurity.util.ApiResponse.ApiUtil;
import com.example.team9_SpringSecurity.util.ApiResponse.CodeSuccess;
import com.example.team9_SpringSecurity.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController                                                                                                         // 컨트롤러 선언
@RequiredArgsConstructor                                                                                                // final 변수, Notnull 표시가 된 변수처럼 필수적인 정보를 세팅하는 생성자를 만든다.
public class MemoController {

    private final MemoService memoService;

    // ----------------- 글 기능
    // 전체글 조회
    @GetMapping("/api/memos")
    public ApiResult getMemos (){
        MessageDto<?> messageDto = memoService.getMemos();
        return ApiUtil.successResponse(CodeSuccess.GET_OK, messageDto);
    }

    // 선택글 조회
    @GetMapping("/api/memos/{id}")
    public ApiResult getMemo (@PathVariable Long id){
        MessageDto<?> messageDto = memoService.getMemos(id);
        return ApiUtil.successResponse(CodeSuccess.GET_OK, messageDto);
    }


    // 글 작성
    @PostMapping("/api/memos")
    public ApiResult createMemo (
            @RequestBody MemoRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {                                                     // @AuthenticationPrincipal 를 통해 유효한 user 정보 객체를 가져와 사용

        MessageDto<?> messageDto = memoService.createMemo(dto, userDetails.getUser());
        return ApiUtil.successResponse(CodeSuccess.CREATE_OK, messageDto);
    }


    // 글 수정
    @PutMapping("/api/memos/{id}")
    public ApiResult modifyMemo(
            @PathVariable Long id,
            @RequestBody MemoRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MessageDto<?> messageDto = memoService.modifyMemo(id, dto, userDetails.getUser());
        return ApiUtil.successResponse(CodeSuccess.MODIFY_OK, messageDto);
    }

    // 글 삭제
    @DeleteMapping("/api/memos/{id}")
    public ApiResult deleteMemo(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MessageDto<?> messageDto = memoService.deleteMemo(id, userDetails.getUser());
        return ApiUtil.successResponse(CodeSuccess.DELETE_OK, messageDto);
    }


    // ---------------댓글 기능
    // 댓글 작성
    @PostMapping("/api/memos/{id}")
    public ApiResult createReply(
            @PathVariable Long id,
            @RequestBody ReplyRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        MessageDto<?> messageDto = memoService.createReply(id, dto, userDetails.getUser());
        return ApiUtil.successResponse(CodeSuccess.CREATE_OK, messageDto);
    }

    // 댓글 수정
    @PutMapping("/api/memos/{id}/{replyId}")
    public ApiResult modifyReply(
            @PathVariable Long id,
            @PathVariable Long replyId,
            @RequestBody ReplyRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        MessageDto<?> messageDto = memoService.modifyReply(id, replyId, dto, userDetails.getUser());
        return ApiUtil.successResponse(CodeSuccess.MODIFY_OK, messageDto);
    }

    // 댓글 삭제
    @DeleteMapping("/api/memos/{id}/{replyId}")
    public ApiResult DeleteReply(
            @PathVariable Long id,
            @PathVariable Long replyId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        MessageDto<?> messageDto = memoService.deleteReply(id, replyId, userDetails.getUser());
        return ApiUtil.successResponse(CodeSuccess.DELETE_OK, messageDto);
    }

    // ---------------좋아요 기능
    // 글 좋아요 추가/삭제 기능
    @PutMapping("/api/memos/{id}/like")
    public ApiResult MemoLike(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        MessageDto<?> messageDto = memoService.SetMemoLike(id, userDetails.getUser());
        return ApiUtil.successResponse(CodeSuccess.MODIFY_OK, messageDto);
    }
  

    // 댓글 좋아요 추가/삭제 기능
    @PutMapping("/api/memos/{id}/{replyId}/like")
     public ApiResult ReplyLike(
            @PathVariable Long id,
            @PathVariable Long replyId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        MessageDto<?> messageDto = memoService.SetReplyLike(id, replyId, userDetails.getUser());
        return ApiUtil.successResponse(CodeSuccess.MODIFY_OK, messageDto);
    }
}