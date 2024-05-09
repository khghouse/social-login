package com.login.component;

import com.login.response.KakaoLoginToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class KakaoLogin {

    private final String GRANT_TYPE = "authorization_code";

    @Value("${login.kakao.client.id}")
    private String clientId;

    @Value("${login.kakao.client.secret}")
    private String clientSecret;

    @Value("${login.kakao.url.login}")
    private String loginUrl;

    @Value("${login.kakao.url.callback}")
    private String callbackUrl;

    @Value("${login.kakao.url.authentication}")
    private String authenticationUrl;

    /**
     * 카카오 로그인 URL 생성
     */
    public Object generateLoginUrl() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(loginUrl)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", callbackUrl)
                .queryParam("state", clientSecret)
                .build();

        return uriComponents.toUriString();
    }

    /**
     * 카카오 로그인 인증
     */
    public KakaoLoginToken authentication(String code) {

        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .baseUrl(authenticationUrl)
                .build();

        return webClient.post()
                .body(BodyInserters.fromFormData("grant_type", GRANT_TYPE)
                        .with("client_id", clientId)
                        .with("redirect_uri", callbackUrl)
                        .with("code", code)
                        .with("client_secret", clientSecret))
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(KakaoLoginToken.class);
                    } else {
                        throw new RuntimeException("정상 처리되지 못했습니다.");
                    }
                }).block();
    }

}
