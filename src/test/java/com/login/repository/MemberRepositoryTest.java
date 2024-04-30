package com.login.repository;

import com.login.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("SNS 로그인을 통해 가입한 회원의 소셜 ID로 유효한 회원을 조회한다.")
    void findBySocialIdAndDeletedFalse() {
        // given
        Member member = Member.builder()
                .type((short) 2)
                .email("khghouse@naver.com")
                .socialId("naver12345")
                .deleted(false)
                .build();

        memberRepository.save(member);

        // when
        Member result = memberRepository.findBySocialIdAndDeletedFalse("naver12345")
                .orElseThrow(RuntimeException::new);

        // then
        assertThat(result.getId()).isEqualTo(member.getId());
        assertThat(result.getSocialId()).isEqualTo("naver12345");
    }

    @Test
    @DisplayName("SNS 로그인을 통해 가입하고 탈퇴한 회원의 소셜 ID로 회원을 조회하면 예외가 발생한다.")
    void findBySocialIdAndDeletedFalseLeaveMember() {
        // given
        Member member = Member.builder()
                .type((short) 2)
                .email("khghouse@naver.com")
                .socialId("naver12345")
                .deleted(true)
                .build();

        memberRepository.save(member);

        // when, then
        assertThatThrownBy(() -> memberRepository.findBySocialIdAndDeletedFalse("naver12345")
                .orElseThrow(RuntimeException::new))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("미가입 회원을 소셜 ID로 조회하면 예외가 발생한다.")
    void findBySocialIdAndDeletedFalseNotExist() {
        // when, then
        assertThatThrownBy(() -> memberRepository.findBySocialIdAndDeletedFalse("naver12345")
                .orElseThrow(RuntimeException::new))
                .isInstanceOf(RuntimeException.class);
    }

}