package com.login.api;

import com.login.request.AuthRequest;
import com.login.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * SNS 회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Validated AuthRequest request) {
        authService.signup(request.toServiceRequest());
        return ResponseEntity.ok(null);
    }

}
