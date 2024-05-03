package com.login.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthLoginResponse {

    private final Long memberId;
    private final String accessToken;
    private final String socialId;
    private final String email;

    public static OAuthLoginResponse of(String socialId, String email) {
        return OAuthLoginResponse.builder()
                .socialId(socialId)
                .email(email)
                .build();
    }

    public static OAuthLoginResponse of(Long memberId, String accessToken) {
        return OAuthLoginResponse.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .build();
    }

}
