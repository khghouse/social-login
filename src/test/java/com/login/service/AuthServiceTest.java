package com.login.service;

import com.login.IntegrationTestSupport;
import com.login.entity.Member;
import com.login.repository.MemberRepository;
import com.login.request.AuthServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class AuthServiceTest extends IntegrationTestSupport {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 가입에 성공한다.")
    void signup() {
        // given
        AuthServiceRequest request = AuthServiceRequest.builder()
                .socialId("naver12345")
                .email("khghouse@naver.com")
                .refreshToken("refreshToken")
                .build();

        // when
        authService.signup(request);

        // then
        Member result = memberRepository.findBySocialIdAndDeletedFalse("naver12345")
                .orElseThrow(() -> new RuntimeException("존재하지 않는 계정입니다."));
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("회원 가입을 시도했지만 이미 가입된 회원으로 예외가 발생한다.")
    void signupAlreadyJoinedMember() {
        // given
        Member member = Member.builder()
                .socialId("naver12345")
                .email("khghouse@naver.com")
                .refreshToken("refreshToken")
                .deleted(false)
                .build();

        memberRepository.save(member);

        AuthServiceRequest request = AuthServiceRequest.builder()
                .socialId("naver12345")
                .email("khghouse@naver.com")
                .refreshToken("refreshToken")
                .build();

        // when, then
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 가입된 회원입니다.");
    }

}