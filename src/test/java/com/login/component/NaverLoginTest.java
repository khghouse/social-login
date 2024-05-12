package com.login.component;

import com.login.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NaverLoginTest extends IntegrationTestSupport {

    @Autowired
    private NaverLogin naverLogin;

    @Test
    @DisplayName("네이버 로그인 인증 시, 필수 파라미터 값이 없다면 예외가 발생한다.")
    void authenticationInvalidRequest() {
        // when, then
        assertThatThrownBy(() -> naverLogin.authentication(null, null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("네이버 로그인 인증 시, 코드 값이 없다면 예외가 발생한다.")
    void authenticationNotExistCode() {
        // when, then
        assertThatThrownBy(() -> naverLogin.authentication(null, "M3MMRZp18DnQPY2cvM"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("네이버 로그인 인증 시, 상태 값이 없다면 예외가 발생한다.")
    void authenticationNotExistState() {
        // when, then
        assertThatThrownBy(() -> naverLogin.authentication("M3MMRZp18DnQPY2cvM", null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("네이버 프로필 조회 시, 액세스 토큰이 유효하지 않으면 예외가 발생한다.")
    void getProfileInvalidAccessToken() {
        // when, then
        assertThatThrownBy(() -> naverLogin.profile(null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("네이버 프로필 조회 시, 토큰 타입이 유효하지 않으면 예외가 발생한다.")
    void getProfileInvalidTokenType() {
        // when, then
        assertThatThrownBy(() -> naverLogin.profile("AAAAOBEqex0W37RwtoU7pimWVu7Uk4fB9ipLNlaPYqMjnGFxL9ZHzPqp1UBdNiXAXb7d5N_cQlNEch-cjg_2GI4KrJs"))
                .isInstanceOf(RuntimeException.class);
    }

}