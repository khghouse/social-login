package com.login.api;

import com.login.component.NaverLogin;
import com.login.response.NaverLoginToken;
import com.login.response.NaverProfile;
import com.login.response.NaverProfileResponse;
import com.login.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OAuthController {

    private final NaverLogin naverLogin;
    private final OAuthService oAuthService;

    @GetMapping("/login/naver/callback")
    public ResponseEntity<?> loginNaver(@Param("code") String code, @Param("state") String state) {
        // 네이버 로그인 인증
        NaverLoginToken naverLoginToken = naverLogin.authentication(code, state);

        // 네이버 프로필 조회
        NaverProfileResponse naverLoginResponse = naverLogin.getProfile(naverLoginToken.getAccess_token(), naverLoginToken.getToken_type());
        NaverProfile naverProfile = naverLoginResponse.getResponse();

        // SNS 로그인
        return ResponseEntity.ok(oAuthService.login(naverProfile.getId(), naverProfile.getEmail()));
    }

}
