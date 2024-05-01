package com.login.service;

import com.login.IntegrationTestSupport;
import com.login.entity.Member;
import com.login.repository.MemberRepository;
import com.login.response.OAuthLoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class OAuthServiceTest extends IntegrationTestSupport {

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("SNS 로그인을 통해 가입한 회원이 로그인 후 액세스 토큰을 획득한다.")
    void login() {
        // given
        Member member = Member.builder()
                .type((short) 2)
                .email("khghouse@naver.com")
                .socialId("naver12345")
                .deleted(false)
                .build();

        memberRepository.save(member);

        // when
        OAuthLoginResponse result = oAuthService.login("naver12345");

        // then
        assertThat(result.getMemberId()).isEqualTo(member.getId());
        assertThat(result.getAccessToken()).isEqualTo("accessToken");
        assertThat(result.getSocialId()).isNull();
    }

    @Test
    @DisplayName("미가입한 회원이 로그인을 시도하면 소셜 ID를 응답한다.")
    void loginNotExistMember() {
        // when
        OAuthLoginResponse result = oAuthService.login("naver12345");

        // then
        assertThat(result.getMemberId()).isNull();
        assertThat(result.getAccessToken()).isNull();
        assertThat(result.getSocialId()).isEqualTo("naver12345");
    }

}