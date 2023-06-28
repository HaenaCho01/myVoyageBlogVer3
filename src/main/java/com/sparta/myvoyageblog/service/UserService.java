package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.SignupRequestDto;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.entity.UserRoleEnum;
import com.sparta.myvoyageblog.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    public String signup(SignupRequestDto requestDto, HttpServletResponse response) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);

        if (checkUsername.isPresent()) {
            response.setStatus(400);
            return "중복된 username 입니다.";
//            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
//                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
                response.setStatus(400);
                return "관리자 암호가 틀려 등록이 불가능합니다.";
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
         User user = new User(username, password, role);
        userRepository.save(user);
        return "회원가입에 성공하였습니다";
    }
}