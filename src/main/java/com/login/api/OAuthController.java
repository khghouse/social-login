package com.login.api;

import com.login.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/login/naver/callback")
    public ResponseEntity<?> loginNaver(@Param("code") String code, @Param("state") String state) {
        return ResponseEntity.ok(oAuthService.loginNaver(code, state));
    }

    @GetMapping("/login/kakao/callback")
    public ResponseEntity<?> loginKakao(@Param("code") String code, @Param("state") String state) {
        return ResponseEntity.ok(null);
    }

}
