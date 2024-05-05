package com.login.request;

import com.login.entity.Member;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthServiceRequest {

    private String email;
    private String socialId;
    private String refreshToken;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .socialId(socialId)
                .refreshToken(refreshToken)
                .deleted(false)
                .build();
    }

}
