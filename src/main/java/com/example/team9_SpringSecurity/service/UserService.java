package com.example.team9_SpringSecurity.service;

import com.example.team9_SpringSecurity.dto.LoginRequestDto;
import com.example.team9_SpringSecurity.dto.MessageDto;
import com.example.team9_SpringSecurity.dto.SignupRequestDto;
import com.example.team9_SpringSecurity.entity.User;
import com.example.team9_SpringSecurity.entity.UserRoleEnum;
import com.example.team9_SpringSecurity.repository.UserRepository;
import com.example.team9_SpringSecurity.util.ApiResponse.CodeSuccess;
import com.example.team9_SpringSecurity.util.ApiResponse.CustomException;
import com.example.team9_SpringSecurity.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.example.team9_SpringSecurity.util.ApiResponse.CodeError.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "admin";

    public MessageDto<?> signup(SignupRequestDto dto){
        String username = dto.getUsername();
        String password = passwordEncoder.encode(dto.getPassword());
        UserRoleEnum role = ADMIN_TOKEN.equals(dto.getAdminToken()) ? UserRoleEnum.ADMIN : UserRoleEnum.USER ;

        if(userRepository.findByUsername(username).isPresent()){
            throw new CustomException(EXIST_USER);
        };

        User user = new User(username, password, role);
        userRepository.save(user);
        return new MessageDto<>(CodeSuccess.JOIN_OK);
    }

    public MessageDto<?> login(LoginRequestDto dto, HttpServletResponse response){
        String username = dto.getUsername();
        String password = dto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(LOGIN_MATCH_FAIL)
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {                                                   // 비밀번호 비교
            throw new CustomException(INVALID_PASSWORD);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));  // 메소드사용하려면 의존성주입 먼저

        return new MessageDto<>(CodeSuccess.LOGIN_OK);
    }
    
    @Transactional
    // 회원탈퇴
    public MessageDto delete(LoginRequestDto dto, HttpServletRequest request) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(LOGIN_MATCH_FAIL)
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {                                                   // 비밀번호 비교
            throw new CustomException(LOGIN_MATCH_FAIL);
        }

        String token = jwtUtil.resolveToken(request);

        if (token != null) {
            userRepository.deleteByUsername(username);
            return new MessageDto(CodeSuccess.OK);
        }
        throw new CustomException(BAD_REQUEST_TOKEN);
    }

}