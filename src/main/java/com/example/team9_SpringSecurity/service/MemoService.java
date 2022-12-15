package com.example.team9_SpringSecurity.service;

import com.example.team9_SpringSecurity.dto.*;
import com.example.team9_SpringSecurity.entity.*;

import com.example.team9_SpringSecurity.repository.*;
import com.example.team9_SpringSecurity.util.ApiResponse.CodeSuccess;
import com.example.team9_SpringSecurity.util.ApiResponse.CustomException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static com.example.team9_SpringSecurity.util.ApiResponse.CodeError.*;

@Service
@RequiredArgsConstructor        // 생성자 자동 주입
public class MemoService {

    private final MemoRepository memoRepository;
    private final ReplyRepository replyRepository;
    private final LikeMemoRepository likeMemoRepository;
    private final LikeReplyRepository likeReplyRepository;


    // 전체 글 조회
    public MessageDto<?> getMemos() {                                                       // 부모클래스인 MessageDto로 리턴타입을 정하고 UserDto도 사용해 다형성 사용
        List<Memo> memolist = memoRepository.findAllByOrderByCreatedAtDesc();               // memoList 전체를 생성일자 기준으로 조회
        List<MemoResponseDto> responseDtoList = new ArrayList<>();                          // Dto

        for (Memo memo : memolist) {
            long likeMemo = likeMemoRepository.totalcnt(memo.getMemoId());                      // native-query를 통한 글 좋아요 cnt 개수 조회

            MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();               // Builder-Pattern Dto 생성
            MemoResponseDto responseDto =
                    mrdBuilder.id(memo.getMemoId())
                            .title(memo.getTitle())
                            .username(memo.getUsername())
                            .content(memo.getContent())
                            .createdAt(memo.getCreatedAt())
                            .modifiedAt(memo.getModifiedAt())
                            .likeCnt(likeMemo)
                            .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))       // 댓글 + native-query를 통한 댓글 좋아요 cnt 개수 조회
                            .getMemos();
            responseDtoList.add(responseDto);
        }
        return new MessageDto<>(CodeSuccess.GET_OK, responseDtoList);
    }

    // 선택 글 조회 기능

    public MessageDto<?> getMemos(Long id) {
        Memo memo = memoRepository.findById(id).orElseThrow(                                // 입력받은 id값을 Memorepository에서 검색 & 없을경우 Exception 처리
                () -> new CustomException(MEMO_NOT_FOUND)
        );
        long likeMemo = likeMemoRepository.totalcnt(id);                                        // native-query를 통한 글 좋아요 cnt 개수 조회

        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();                   // Builder-Pattern Dto 생성
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getMemoId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))
                        .likeCnt(likeMemo)
                        .getMemos();
        return new MessageDto<>(CodeSuccess.GET_OK, responseDto);
    }

    // 글 작성 기능

    public MessageDto<?> createMemo(MemoRequestDto dto, User user) {               // dto + Spring Security(userDatailsimple)을 통한 사용자 정보 사용

        Memo memo = new Memo(dto, user);                                        // requestDto + User
        memoRepository.save(memo);                                              // 저장

        long likeMemo = likeMemoRepository.totalcnt(memo.getMemoId());          // native-query를 통한 글 좋아요 cnt 개수 조회
        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getMemoId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))
                        .likeCnt(likeMemo)
                        .getMemos();
        return new MessageDto<>(CodeSuccess.CREATE_OK, responseDto);
    }


    // 글 수정 기능
    @Transactional
    public MessageDto<?> modifyMemo (Long id, MemoRequestDto dto, User user) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        if (memo.getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
            memo.update(dto);  // update는 entity에 새로 정의한 함수
        } else {
            throw new CustomException(NO_ACCESS);
        }

        long likeMemo = likeMemoRepository.totalcnt(id);
        MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
        MemoResponseDto responseDto =
                mrdBuilder.id(memo.getMemoId())
                        .title(memo.getTitle())
                        .username(memo.getUsername())
                        .content(memo.getContent())
                        .createdAt(memo.getCreatedAt())
                        .modifiedAt(memo.getModifiedAt())
                        .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))
                        .likeCnt(likeMemo)
                        .getMemos();

        return new MessageDto<>(CodeSuccess.MODIFY_OK, responseDto);
    }

    // 글 삭제 기능
    @Transactional
    public MessageDto<?> deleteMemo (Long id, User user) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        if (memo.getUsername().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
            memoRepository.deleteById(id);
        } else {
            throw new CustomException(NO_ACCESS);
        }

        return new MessageDto<>(CodeSuccess.DELETE_OK);
    }


    // 댓글 작성 기능
    public MessageDto<?> createReply(Long id, ReplyRequestDto dto, User user) {                // id + dto + Spring Security(userDatailsimple)을 통한 사용자 정보 사용
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply newOne = new Reply(dto, user, memo);
        replyRepository.save(newOne);

        long likeReply = likeReplyRepository.totalcnt(newOne.getReplyId());                  // native-query를 통한 댓글 좋아요 cnt 개수 조회
        ReplyResponseDto responseDto = new ReplyResponseDto(newOne, likeReply);
        return new MessageDto<>(CodeSuccess.CREATE_OK, responseDto);
    }


    // 댓글 수정 기능
    @Transactional
    public MessageDto<?> modifyReply(Long id, Long replyId, ReplyRequestDto dto, User user) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(REPLY_NOT_FOUND)
        );

        if (reply.getReplyName().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
            reply.update(dto);
        } else {
            throw new CustomException(NO_ACCESS);
        }

        long likeReply = likeReplyRepository.totalcnt(replyId);                             // native-query를 통한 댓글 좋아요 cnt 개수 조회
        ReplyResponseDto responseDto = new ReplyResponseDto(reply, likeReply);
        return new MessageDto<>(CodeSuccess.MODIFY_OK, responseDto);
    }


    // 댓글 삭제 기능
    @Transactional
    public MessageDto<?> deleteReply(Long id, Long replyId, User user) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(
                () -> new CustomException(REPLY_NOT_FOUND)
        );

        if (reply.getReplyName().equals(user.getUsername()) || user.getRole() == UserRoleEnum.ADMIN) {
            replyRepository.deleteByReplyId(replyId);
        } else {
            throw new CustomException(NO_ACCESS);
        }

        return new MessageDto<>(CodeSuccess.DELETE_OK);

    }

    @Transactional
    // 글 좋아요 기능 구현
    public MessageDto<?> SetMemoLike(Long id, User user) {                                              // id + Spring Security(userDatailsimple)을 통한 사용자 정보 사용
        Memo memo = memoRepository.findById(id).orElseThrow(                                            // Id와 일치하는 글이 있는지 확인
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Optional<LikeMemo> likes = likeMemoRepository.findAllByUserId(user.getId());                    // likeMemo에서 일치하는 사용자 정보가 있는지 확인

        if (likes.isEmpty()) {                                                                          // 만약 일치하는 정보가 없으면(해당 글에 좋아요를 시도하는 사용자의 정보가 없으면)
            LikeMemo memoLike = new LikeMemo(user, memo);                                               // 좋아요 추가
            likeMemoRepository.save(memoLike);
            long likeMemo = likeMemoRepository.totalcnt(memo.getMemoId());

            MemoResponseDtoBuilder mrdBuilder = new MemoResponseDtoBuilder();
            MemoResponseDto responseDto =
                    mrdBuilder.id(memo.getMemoId())
                            .title(memo.getTitle())
                            .username(memo.getUsername())
                            .content(memo.getContent())
                            .createdAt(memo.getCreatedAt())
                            .modifiedAt(memo.getModifiedAt())
                            .addReply(addLikeCntToReplyResponseDto(memo.getReplies()))
                            .likeCnt(likeMemo)
                            .getMemos();
            return new MessageDto<>(CodeSuccess.MODIFY_OK, responseDto);
        } else {                                                                                        // 일치하는 정보가 있으면(=이미 좋아요를 누른 사용자이면)
            likeMemoRepository.deleteByUserId(user.getId());                                            // 삭제 처리
            return new MessageDto<>(CodeSuccess.MODIFY_OK);
        }
    }


    @Transactional
    // 댓글 좋아요 기능 구현
    public MessageDto<?> SetReplyLike(Long id, Long replyId, User user) {                               // id + Spring Security(userDatailsimple)을 통한 사용자 정보 사용
        Memo memo = memoRepository.findById(id).orElseThrow(                                            // Id와 일치하는 글이 있는지 확인
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Reply reply = replyRepository.findByMemo_MemoIdAndReplyId(id, replyId).orElseThrow(             // Id와 일치하는 댓글이 있는지 확인
                () -> new CustomException(MEMO_NOT_FOUND)
        );

        Optional<LikeReply> likes = likeReplyRepository.findAllByUserId(user.getId());                  // likeReply테이블에서 일치하는 사용자 정보가 있는지 확인

        if (likes.isEmpty()) {                                                                          // 만약 일치하는 정보가 없으면(해당 글에 좋아요를 시도하는 사용자의 정보가 없으면)
            LikeReply likeReply = new LikeReply(user, memo, reply);                                     // 좋아요 추가
            likeReplyRepository.save(likeReply);
            LikeResponseDto responseDto = new LikeResponseDto(likeReply);
            return new MessageDto<>(CodeSuccess.MODIFY_OK, responseDto);
        } else {                                                                                        // 일치하는 정보가 있으면(=이미 좋아요를 누른 사용자이면)
            likeReplyRepository.deleteByMemoMemoId(id);                                                 // 삭제 처리
            return new MessageDto<>(CodeSuccess.MODIFY_OK);
        }
    }

    // 댓글 리스트 + 댓글 cnt 조회 기능
    public List<ReplyResponseDto> addLikeCntToReplyResponseDto(List<Reply> replies) {
        List<ReplyResponseDto> exportReplies = new ArrayList<>();
        for (int i = 0; i < replies.size(); i++) {
            Long replyId = replies.get(i).getReplyId();
            Long likeReply = likeReplyRepository.totalcnt(replyId);
            exportReplies.add(new ReplyResponseDto(replies.get(i), likeReply));
        }
        return exportReplies;
    }
}