package com.login.component;

import com.login.enumeration.LoginType;
import com.login.response.GoogleLoginToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GoogleLogin implements LoginStrategy<GoogleLoginToken, Object> {

    @Value("${login.google.client.id}")
    private String clientId;

    @Value("${login.google.client.secret}")
    private String clientSecret;

    @Value("${login.google.url.login}")
    private String loginUrl;

    @Value("${login.google.url.callback}")
    private String callbackUrl;

    @Value("${login.google.url.authentication}")
    private String authenticationUrl;

    @Value("${login.google.url.scope}")
    private String scope;

    @Override
    public LoginType getLoginType() {
        return LoginType.GOOGLE;
    }


    /**
     * 구글 로그인 URL 생성
     */
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

    /**
     * 구글 로그인 인증
     */
    @Override
    public GoogleLoginToken authentication(String code, String state) {
        String GRANT_TYPE = "authorization_code";
        return WebClient.create()
                .post()
                .uri(authenticationUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("grant_type", GRANT_TYPE)
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("code", code)
                        .with("redirect_uri", callbackUrl))
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(GoogleLoginToken.class);
                    } else {
                        throw new RuntimeException(String.format("[%s] 정상 처리되지 못했습니다.", response.statusCode()));
                    }
                }).block();
    }

    @Override
    public GoogleLoginToken authentication(String refreshToken) {
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
