package com.login.response;

import com.login.enumeration.LoginType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GoogleLoginToken {

    private final LoginType loginType = LoginType.GOOGLE;

    private String access_token;
    private Integer expires_in;
    private String token_type;
    private String scope;
    private String refresh_token;

}
