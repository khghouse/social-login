package com.login.service;

import com.login.component.NaverLogin;
import com.login.entity.Member;
import com.login.repository.MemberRepository;
import com.login.response.NaverLoginToken;
import com.login.response.NaverProfile;
import com.login.response.NaverProfileResponse;
import com.login.response.OAuthLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthService {

    private final NaverLogin naverLogin;
    private final MemberRepository memberRepository;

    /**
     * 네이버 로그인
     */
    @Transactional
    public OAuthLoginResponse loginNaver(String code, String state) {
        // 네이버 로그인 인증
        NaverLoginToken naverLoginToken = naverLogin.authentication(code, state);

        // 네이버 프로필 조회
        NaverProfileResponse naverLoginResponse = naverLogin.naverProfile(naverLoginToken.getAccess_token(), naverLoginToken.getToken_type());
        NaverProfile naverProfile = naverLoginResponse.getResponse();

        // SNS 로그인
        return login(naverProfile.getId(), naverProfile.getEmail(), naverLoginToken.getRefresh_token());
    }

    /**
     * 로그인 처리
     */
    private OAuthLoginResponse login(String id, String email, String refreshToken) {
        // 프로필 조회 -> 고유 식별 값 획득
        Optional<Member> optionalMember = memberRepository.findBySocialIdAndDeletedFalse(id);

        // 가입 정보가 없다면
        if (optionalMember.isEmpty()) {
            // 고유 식별 값만 리턴 -> 클라이언트에서 회원 가입 프로세스 진행
            return OAuthLoginResponse.of(id, email, refreshToken);
        }

        // 가입 회원이면 회원 ID, 액세스 토큰 리턴
        return OAuthLoginResponse.of(optionalMember.get().getId(), "accessToken"); // TODO :: 인증 전략에 맞는 액세스 토큰 구현
    }

}
