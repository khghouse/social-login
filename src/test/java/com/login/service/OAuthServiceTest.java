package com.login.service;

import com.login.IntegrationTestSupport;
import com.login.component.NaverLogin;
import com.login.entity.Member;
import com.login.repository.MemberRepository;
import com.login.response.NaverLoginToken;
import com.login.response.NaverProfile;
import com.login.response.NaverProfileResponse;
import com.login.response.OAuthLoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

@Transactional
class OAuthServiceTest extends IntegrationTestSupport {

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private NaverLogin naverLogin;

    @Test
    @DisplayName("네이버 로그인 인증 성공 후, 아직 회원 가입되지 않은 상태를 확인한다.")
    void loginNaver() {
        // given
        BDDMockito.given(naverLogin.authentication(anyString(), anyString()))
                .willReturn(NaverLoginToken.builder()
                        .access_token("accessToken")
                        .refresh_token("refreshToken")
                        .token_type("bearer")
                        .expires_in(3600)
                        .build());

        NaverProfile naverProfile = NaverProfile.builder()
                .id("naver12345")
                .email("khghouse@naver.com")
                .build();

        BDDMockito.given(naverLogin.naverProfile(anyString(), anyString()))
                .willReturn(NaverProfileResponse.builder()
                        .resultcode("00")
                        .message("success")
                        .response(naverProfile)
                        .build());

        // when
        OAuthLoginResponse result = oAuthService.loginNaver("code", "state");

        // then
        assertThat(result.getSocialId()).isEqualTo("naver12345");
        assertThat(result.getEmail()).isEqualTo("khghouse@naver.com");
        assertThat(result.getRefreshToken()).isEqualTo("refreshToken");
        assertThat(result.getMemberId()).isNull();
        assertThat(result.getAccessToken()).isNull();
    }

    @Test
    @DisplayName("네이버 로그인 인증 성공 후, 이미 가입된 회원임을 확인한다.")
    void loginNaverExistMember() {
        // given
        Member member = Member.builder()
                .socialId("naver12345")
                .refreshToken("refreshToken")
                .deleted(false)
                .build();

        memberRepository.save(member);

        BDDMockito.given(naverLogin.authentication(anyString(), anyString()))
                .willReturn(NaverLoginToken.builder()
                        .access_token("accessToken")
                        .refresh_token("refreshToken")
                        .token_type("bearer")
                        .expires_in(3600)
                        .build());

        NaverProfile naverProfile = NaverProfile.builder()
                .id("naver12345")
                .email("khghouse@naver.com")
                .build();

        BDDMockito.given(naverLogin.naverProfile(anyString(), anyString()))
                .willReturn(NaverProfileResponse.builder()
                        .resultcode("00")
                        .message("success")
                        .response(naverProfile)
                        .build());

        // when
        OAuthLoginResponse result = oAuthService.loginNaver("code", "state");

        // then
        assertThat(result.getMemberId()).isEqualTo(member.getId());
        assertThat(result.getAccessToken()).isEqualTo("accessToken");
        assertThat(result.getSocialId()).isNull();
        assertThat(result.getEmail()).isNull();
        assertThat(result.getRefreshToken()).isNull();
    }

}