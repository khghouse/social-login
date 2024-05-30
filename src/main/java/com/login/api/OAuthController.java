package com.login.api;

import com.login.component.GoogleLogin;
import com.login.response.GoogleLoginToken;
import com.login.response.LoginCallback;
import com.login.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OAuthController {

    private final OAuthService oAuthService;
    private final GoogleLogin googleLogin;

    @GetMapping("/login/naver/callback")
    public ResponseEntity<?> loginNaver(LoginCallback request) {
        return ResponseEntity.ok(oAuthService.loginNaver(request.getCode(), request.getState()));
    }

    @GetMapping("/login/kakao/callback")
    public ResponseEntity<?> loginKakao(LoginCallback request) {
        // callback uri 호출 오류
        if (request.getError() != null) {
            throw new RuntimeException(request.getError_description());
        }

        return ResponseEntity.ok(oAuthService.loginKakao(request.getCode(), request.getState()));
    }

    @GetMapping("/login/google/callback")
    public ResponseEntity<?> loginGoogle(LoginCallback request) {
        GoogleLoginToken googleLoginToken = googleLogin.authentication(request.getCode(), request.getState());
        log.info("{}", googleLoginToken.getAccess_token());
        log.info("{}", googleLoginToken.getRefresh_token());
        log.info("{}", googleLoginToken.getScope());
        return ResponseEntity.ok(null);
    }

}
