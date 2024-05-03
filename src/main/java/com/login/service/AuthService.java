package com.login.service;

import com.login.entity.Member;
import com.login.repository.MemberRepository;
import com.login.request.AuthServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public void signup(AuthServiceRequest request) {
        validateAlreadyJoinedMember(request.getSocialId());
        memberRepository.save(request.toEntity());
    }

    /**
     * 이미 가입된 회원인지 체크 by socialId
     */
    private void validateAlreadyJoinedMember(String socialId) {
        Optional<Member> optMember = memberRepository.findBySocialIdAndDeletedFalse(socialId);

        if (optMember.isPresent()) {
            throw new RuntimeException("이미 가입된 회원입니다.");
        }
    }

}
