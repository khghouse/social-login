package com.login.api;

import com.login.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> withdrawal(@PathVariable Long id) {
        // TODO :: 액세스 토큰 유효성 체크 또는 필터에서 이미 체크

        memberService.withdrawal(id);
        return ResponseEntity.ok(null);
    }

}
