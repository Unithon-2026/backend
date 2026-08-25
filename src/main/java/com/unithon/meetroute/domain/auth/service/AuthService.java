package com.unithon.meetroute.domain.auth.service;

import com.unithon.meetroute.domain.auth.dto.LoginRequest;
import com.unithon.meetroute.domain.auth.dto.LoginResponse;
import com.unithon.meetroute.domain.auth.dto.SignupRequest;
import com.unithon.meetroute.domain.auth.dto.SignupResponse;
import com.unithon.meetroute.domain.user.entity.User;
import com.unithon.meetroute.domain.user.repository.UserRepository;
import com.unithon.meetroute.global.exception.BusinessException;
import com.unithon.meetroute.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phone(request.phone())
                .assignedRegionId(request.assignedRegionId())
                .build();

        return SignupResponse.from(userRepository.save(user));
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        return LoginResponse.from(user);
    }
}
