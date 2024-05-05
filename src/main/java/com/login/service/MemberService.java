package com.login.service;

import com.login.component.NaverLogin;
import com.login.entity.Member;
import com.login.repository.MemberRepository;
import com.login.response.NaverLoginToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final NaverLogin naverLogin;

    @Transactional
    public void withdrawal(Long id) {
        // 이미 탈퇴한 회원인지 체크
        Member member = memberRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("이미 탈퇴한 회원입니다."));

        // 회원 탈퇴 처리
        member.withdrawal();

        // 네이버 연결 서비스 해제를 위한 네이버 로그인 인증 by refreshToken
        NaverLoginToken naverLoginToken = naverLogin.authentication(member.getRefreshToken());

        // 네이버 연결 서비스 해제
        naverLogin.delete(naverLoginToken.getAccess_token());

        // TODO :: 액세스 토큰 말료 처리
    }

}
