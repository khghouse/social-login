package com.login.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class KakaoLogin {

    @Value("${login.kakao.client.id}")
    private String clientId;

    @Value("${login.kakao.url.login}")
    private String loginUrl;

    @Value("${login.kakao.url.callback}")
    private String callbackUrl;

    @Value("${login.kakao.client.state}")
    private String state;

    public Object generateLoginUrl() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(loginUrl)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", callbackUrl)
                .queryParam("state", state)
                .build();

        return uriComponents.toUriString();
    }

}
