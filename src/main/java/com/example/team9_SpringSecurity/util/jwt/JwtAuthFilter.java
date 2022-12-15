package com.example.team9_SpringSecurity.util.jwt;

import com.example.team9_SpringSecurity.util.ApiResponse.CustomException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.example.team9_SpringSecurity.util.ApiResponse.CodeError.INVALID_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // FilterChain : 요청(Request)과 응답(Response)에 대한 정보들을 변경할 수 있게 개발자들에게 제공하는 서블린 컨테이너
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 요청에서 받아온 토큰의 문자열 받기
        String token = jwtUtil.resolveToken(request);

        // 2. 토큰 유효 판별
        if(token != null) {
            if(!jwtUtil.validateToken(token)){
                throw new CustomException(INVALID_TOKEN);                                                               // 커스텀한 실패 메세지로 보내고 싶었는데 다음 필터로 넘어가 실패
            }
            // 3. 토큰이 유효하다면 토큰에서 정보를 가져와 Authentication에 세팅
            Claims info = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(info.getSubject());
        }
        // 4. 다음 필터로 넘어간다
        filterChain.doFilter(request, response);
    }

    // SecurityContextHolder > SecurityContext > Authentication 설정
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
