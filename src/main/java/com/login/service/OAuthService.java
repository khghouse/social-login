package com.login.service;

import com.login.component.KakaoLogin;
import com.login.component.NaverLogin;
import com.login.entity.Member;
import com.login.enumeration.LoginType;
import com.login.repository.MemberRepository;
import com.login.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthService {

    private final MemberRepository memberRepository;
    private final NaverLogin naverLogin;
    private final KakaoLogin kakaoLogin;

    /**
     * 네이버 로그인
     */
    @Transactional
    public OAuthLoginResponse loginNaver(String code, String state) {
        // 네이버 로그인 인증
        NaverLoginToken naverLoginToken = naverLogin.authentication(code, state);

        // 네이버 프로필 조회
        NaverProfileResponse naverLoginResponse = naverLogin.profile(naverLoginToken.getAccess_token());
        NaverProfile naverProfile = naverLoginResponse.getResponse();

        // SNS 로그인
        return login(naverProfile.getId(), naverProfile.getEmail(), naverLoginToken.getRefresh_token(), naverLoginToken.getLoginType());
    }

    /**
     * 카카오 로그인
     */
    @Transactional
    public OAuthLoginResponse loginKakao(String code, String state) {
        // 카카오 로그인 인증
        KakaoLoginToken kakaoLoginToken = kakaoLogin.authentication(code, state);

        // 카카오 프로필 조회
        KakaoProfileResponse profile = kakaoLogin.profile(kakaoLoginToken.getAccess_token());

        // SNS 로그인
        return login(profile.getId(), profile.getKakao_account().getEmail(), kakaoLoginToken.getRefresh_token(), kakaoLoginToken.getLoginType());
    }

    /**
     * 로그인 처리
     */
    private OAuthLoginResponse login(String id, String email, String refreshToken, LoginType loginType) {
        // 프로필 조회 -> 고유 식별 값 획득
        Optional<Member> optionalMember = memberRepository.findBySocialIdAndDeletedFalse(id);

        // 가입 정보가 없다면
        if (optionalMember.isEmpty()) {
            // 고유 식별 값만 리턴 -> 클라이언트에서 회원 가입 프로세스 진행
            return OAuthLoginResponse.of(id, email, refreshToken, loginType.getId());
        }

        // 가입 회원이면 회원 ID, 액세스 토큰 리턴
        return OAuthLoginResponse.of(optionalMember.get().getId(), "accessToken"); // TODO :: 인증 전략에 맞는 액세스 토큰 구현
    }

}
