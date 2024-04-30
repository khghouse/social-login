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
    private final String socialId;
    private final String accessToken;

    public static OAuthLoginResponse of(String socialId) {
        return OAuthLoginResponse.builder()
                .socialId(socialId)
                .build();
    }

    public static OAuthLoginResponse of(Long memberId, String accessToken) {
        return OAuthLoginResponse.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .build();
    }

}
