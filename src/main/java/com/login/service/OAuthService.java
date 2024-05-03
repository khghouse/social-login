package com.login.service;

import com.login.entity.Member;
import com.login.repository.MemberRepository;
import com.login.response.OAuthLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final MemberRepository memberRepository;

    public OAuthLoginResponse login(String id, String email) {
        // 프로필 조회 -> 고유 식별 값 획득
        Optional<Member> optionalMember = memberRepository.findBySocialIdAndDeletedFalse(id);

        // 가입 정보가 없다면
        if (optionalMember.isEmpty()) {
            // 고유 식별 값만 리턴 -> 클라이언트에서 회원 가입 프로세스 진행
            return OAuthLoginResponse.of(id, email);
        }

        // 가입 회원이면 회원 ID, 액세스 토큰 리턴
        return OAuthLoginResponse.of(optionalMember.get().getId(), "accessToken"); // TODO :: 인증 전략에 맞는 액세스 토큰 구현
    }

}
