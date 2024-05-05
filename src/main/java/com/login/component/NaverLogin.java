package com.login.component;

import com.login.response.NaverLoginToken;
import com.login.response.NaverProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class NaverLogin {

    private final String SPACE = " ";

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

        // 네이버 로그인 인증 API 호출
        NaverLoginToken naverLoginToken = webClient.get()
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(NaverLoginToken.class);
                    } else {
                        throw new RuntimeException("로그인에 실패했습니다. 다시 로그인 해주세요.");
                    }
                })
                .block();

        log.info("{}", naverLoginToken.getAccess_token());
        log.info("{}", naverLoginToken.getRefresh_token());

        // 네이버 로그인 인증 API는 에러가 발생해도 200으로 응답 -> error, error_message 값이 존재하면 예외 처리 필요
        if (naverLoginToken.getError() != null) {
            throw new RuntimeException(String.format("[%s] %s", naverLoginToken.getError(), naverLoginToken.getError_description()));
        }

        return naverLoginToken;
    }

    /**
     * 네이버 프로필 조회
     */
    public NaverProfileResponse getProfile(String accessToken, String tokenType) {
        WebClient webClient = WebClient.builder()
                .baseUrl(profileUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, tokenType + SPACE + accessToken)
                .build();

        // 네이버 프로필 조회 API 호출
        NaverProfileResponse naverProfileResponse = webClient.get()
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(NaverProfileResponse.class);
                    }
                    throw new RuntimeException(String.format("[%s] 로그인 할 수 없습니다.", response.statusCode()));
                }).block();

        return naverProfileResponse;
    }

    /**
     * 네이버 로그인 인증 by refreshToken
     */
    public NaverLoginToken authentication(String refreshToken) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(authenticationUrl)
                .queryParam("grant_type", "refresh_token")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("refresh_token", refreshToken)
                .build();

        WebClient webClient = WebClient.builder()
                .baseUrl(uriComponents.toUriString())
                .build();

        // 네이버 로그인 인증 API 호출
        NaverLoginToken naverLoginToken = webClient.get()
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(NaverLoginToken.class);
                    } else {
                        throw new RuntimeException("로그인에 실패했습니다. 다시 로그인 해주세요.");
                    }
                })
                .block();

        log.info("{}", naverLoginToken.getAccess_token());
        log.info("{}", naverLoginToken.getRefresh_token());

        // 네이버 로그인 인증 API는 에러가 발생해도 200으로 응답 -> error, error_message 값이 존재하면 예외 처리 필요
        if (naverLoginToken.getError() != null) {
            throw new RuntimeException(String.format("[%s] %s", naverLoginToken.getError(), naverLoginToken.getError_description()));
        }

        return naverLoginToken;
    }

    /**
     * 네이버 로그인 액세스 토큰 삭제
     */
    public NaverLoginToken delete(String accessToken) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(authenticationUrl)
                .queryParam("grant_type", "delete")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("access_token", accessToken)
                .queryParam("service_provider", "NAVER")
                .build();

        log.info("{}", uriComponents.toUriString());

        WebClient webClient = WebClient.builder()
                .baseUrl(uriComponents.toUriString())
                .build();

        // 네이버 로그인 액세스 토큰 삭제 API 호출
        NaverLoginToken naverLoginToken = webClient.get()
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(NaverLoginToken.class);
                    } else {
                        throw new RuntimeException("액세스 토큰 삭제에 실패했습니다.");
                    }
                })
                .block();

        log.info("{}", naverLoginToken.getAccess_token());
        log.info("{}", naverLoginToken.getRefresh_token());
        log.info("{}", naverLoginToken.getError());
        log.info("{}", naverLoginToken.getError_description());

        // 에러가 발생해도 200으로 응답 -> error, error_message 값이 존재하면 예외 처리 필요
        if (naverLoginToken.getError() != null) {
            throw new RuntimeException(String.format("[%s] %s", naverLoginToken.getError(), naverLoginToken.getError_description()));
        }

        return naverLoginToken;
    }

}
