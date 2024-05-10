package com.login.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoProfileResponse {

    private String id;
    private LocalDateTime connected_at;
    private KakaoAccount kakao_account;

}
