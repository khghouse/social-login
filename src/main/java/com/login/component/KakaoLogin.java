package com.login.component;

import com.login.response.KakaoLoginToken;
import com.login.response.KakaoProfileResponse;
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
public class KakaoLogin implements LoginStrategy<KakaoLoginToken, KakaoProfileResponse> {

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

    @Value("${login.kakao.url.profile}")
    private String profileUrl;

    @Value("${login.kakao.url.disconnect}")
    private String disconnectUrl;

    /**
     * 카카오 로그인 URL 생성
     */
    public String loginUrl() {
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
    public KakaoLoginToken authentication(String code, String state) {
        String GRANT_TYPE = "authorization_code";
        return WebClient.create()
                .post()
                .uri(authenticationUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("grant_type", GRANT_TYPE)
                        .with("client_id", clientId)
                        .with("redirect_uri", callbackUrl)
                        .with("code", code)
                        .with("client_secret", clientSecret))
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(KakaoLoginToken.class);
                    } else {
                        throw new RuntimeException(String.format("[%s] 정상 처리되지 못했습니다.", response.statusCode()));
                    }
                }).block();
    }

    /**
     * 카카오 로그인 인증 by refreshToken
     */
    public KakaoLoginToken authentication(String refreshToken) {
        return WebClient.create()
                .post()
                .uri(authenticationUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", clientId)
                        .with("refresh_token", refreshToken)
                        .with("client_secret", clientSecret))
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(KakaoLoginToken.class);
                    } else {
                        throw new RuntimeException(String.format("[%s] 정상 처리되지 못했습니다.", response.statusCode()));
                    }
                }).block();
    }

    /**
     * 카카오 로그인 연결 해제
     */
    public void disconnect(String accessToken) {
        WebClient.create()
                .post()
                .uri(disconnectUrl)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE_BEARER + accessToken)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(Long.class);
                    } else {
                        throw new RuntimeException(String.format("[%s] 정상 처리되지 못했습니다.", response.statusCode()));
                    }
                }).block();
    }

    /**
     * 카카오 프로필 조회
     */
    public KakaoProfileResponse profile(String accessToken) {
        return WebClient.create()
                .get()
                .uri(profileUrl)
                .headers(headers -> {
                    headers.add(HttpHeaders.AUTHORIZATION, TOKEN_TYPE_BEARER + accessToken);
                    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
                }).exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(KakaoProfileResponse.class);
                    } else {
                        throw new RuntimeException(String.format("[%s] 정상 처리되지 못했습니다.", response.statusCode()));
                    }
                }).block();
    }

}
