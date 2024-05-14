package com.login.service;

import com.login.component.LoginStrategy;
import com.login.component.LoginStrategyFactory;
import com.login.entity.Member;
import com.login.enumeration.LoginType;
import com.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final LoginStrategyFactory loginStrategyFactory;

    @Transactional
    public void withdrawal(Long id) {
        // 이미 탈퇴한 회원인지 체크
        Member member = memberRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("이미 탈퇴한 회원입니다."));

        // 회원 탈퇴 처리
        member.withdrawal();

        // 소셜 로그인 서비스 연결 해제
        LoginType loginType = LoginType.of(member.getType());
        LoginStrategy loginStrategy = loginStrategyFactory.getStrategy(loginType);
        loginStrategy.disconnect(member.getRefreshToken()); // TODO :: 소셜 로그인 후 응답 받은 리프레쉬 토큰을 DB 또는 Redis에서 조회

        // TODO :: 서비스 액세스 토큰 만료 처리
    }

}
