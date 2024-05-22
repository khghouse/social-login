package com.login.component;

import com.login.enumeration.LoginType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GoogleLogin implements LoginStrategy {

    @Value("${login.google.client.id}")
    private String clientId;

    @Value("${login.google.client.secret}")
    private String clientSecret;

    @Value("${login.google.url.login}")
    private String loginUrl;

    @Value("${login.google.url.callback}")
    private String callbackUrl;

    @Value("${login.google.url.scope}")
    private String scope;

    @Override
    public LoginType getLoginType() {
        return LoginType.GOOGLE;
    }


    @Override
    public String loginUrl() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(loginUrl)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("scope", scope)
                .queryParam("redirect_uri", callbackUrl)
                .build();

        return uriComponents.toUriString();
    }

    @Override
    public Object authentication(String code, String state) {
        return null;
    }

    @Override
    public Object authentication(String refreshToken) {
        return null;
    }

    @Override
    public void disconnect(String refreshToken) {

    }

    @Override
    public Object profile(String accessToken) {
        return null;
    }

}
