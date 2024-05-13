package com.login.response;

import com.login.enumeration.LoginType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NaverLoginToken {

    private final LoginType loginType = LoginType.NAVER;

    private String access_token;
    private String refresh_token;
    private String token_type;
    private Integer expires_in;
    private String error;
    private String error_description;

}
