package com.login.service;

import com.login.IntegrationTestSupport;
import com.login.component.NaverLogin;
import com.login.entity.Member;
import com.login.repository.MemberRepository;
import com.login.response.NaverLoginToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;

@Transactional
class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private NaverLogin naverLogin;

    @Test
    @DisplayName("회원 탈퇴 후 확인한다.")
    void withdrawal() {
        // given
        Member member = Member.builder()
                .refreshToken("refreshToken")
                .deleted(false)
                .build();

        memberRepository.save(member);

        BDDMockito.given(naverLogin.authentication(anyString()))
                .willReturn(NaverLoginToken.builder()
                        .access_token("accessToken")
                        .build());

        BDDMockito.doNothing()
                .when(naverLogin)
                .disconnect(anyString());

        // when
        memberService.withdrawal(member.getId());

        // then
        Member result = memberRepository.findById(member.getId())
                .orElseThrow(RuntimeException::new);

        assertThat(result.getRefreshToken()).isEqualTo("refreshToken");
        assertThat(result.getDeleted()).isTrue();
    }

    @Test
    @DisplayName("회원 탈퇴를 시도했지만 이미 탈퇴한 회원이라 예외가 발생한다.")
    void withdrawalAlreadyLeave() {
        // given
        Member member = Member.builder()
                .deleted(true)
                .build();

        memberRepository.save(member);

        // when, then
        assertThatThrownBy(() -> memberService.withdrawal(member.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 탈퇴한 회원입니다.");
    }

}