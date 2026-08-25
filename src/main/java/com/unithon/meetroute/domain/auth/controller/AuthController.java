package com.unithon.meetroute.domain.auth.controller;

import com.unithon.meetroute.domain.auth.SessionConst;
import com.unithon.meetroute.domain.auth.dto.LoginRequest;
import com.unithon.meetroute.domain.auth.dto.LoginResponse;
import com.unithon.meetroute.domain.auth.dto.SignupRequest;
import com.unithon.meetroute.domain.auth.dto.SignupResponse;
import com.unithon.meetroute.domain.auth.service.AuthService;
import com.unithon.meetroute.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 컨트롤러")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "영업사원 계정을 생성합니다.")
    public ApiResponse<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ApiResponse.success(authService.signup(request));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일/비밀번호로 로그인하고 세션을 발급합니다.")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        LoginResponse response = authService.login(request);

        HttpSession session = servletRequest.getSession();
        session.setAttribute(SessionConst.LOGIN_USER_ID, response.id());

        return ApiResponse.success(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "현재 세션을 만료시킵니다.")
    public ApiResponse<Void> logout(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ApiResponse.success(null);
    }
}
