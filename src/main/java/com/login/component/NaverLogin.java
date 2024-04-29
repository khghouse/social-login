package com.login.component;

import com.login.response.NaverLoginToken;
import com.login.response.NaverProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NaverLogin {

    private final String BLANK = " ";

    @Value("${login.naver.client.id}")
    private String clientId;

    @Value("${login.naver.client.secret}")
    private String clientSecret;

    @Value("${login.naver.url.login}")
    private String loginUrl;

    @Value("${login.naver.url.callback}")
    private String callbackUrl;

    @Value("${login.naver.url.authentication}")
    private String authenticationUrl;

    @Value("${login.naver.url.profile}")
    private String profileUrl;

    /**
     * 네이버 로그인 URL 생성
     */
    public String generateLoginUrl() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(loginUrl)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("state", clientSecret)
                .queryParam("redirect_uri", callbackUrl)
                .build();

        return uriComponents.toUriString();
    }

    /**
     * 네이버 로그인 인증
     */
    public NaverLoginToken authentication(String code, String state) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(authenticationUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .queryParam("state", state)
                .build();

        WebClient webClient = WebClient.builder()
                .baseUrl(uriComponents.toUriString())
                .build();

        return webClient.get()
                .retrieve()
                .bodyToMono(NaverLoginToken.class)
                .block();
    }

    /**
     * 네이버 프로필 조회
     */
    public NaverProfileResponse getProfile(String accessToken, String tokenType) {
        WebClient webClient = WebClient.builder()
                .baseUrl(profileUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, tokenType + BLANK + accessToken)
                .build();

        return webClient.get()
                .retrieve()
                .bodyToMono(NaverProfileResponse.class)
                .block();
    }

}
