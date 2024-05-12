package com.login.component;

public interface LoginStrategy<T, P> {

    String SPACE = " ";
    String TOKEN_TYPE_BEARER = "Bearer" + SPACE;

    String loginUrl();

    T authentication(String code, String state);

    T authentication(String refreshToken);

    void disconnect(String accessToken);

    P profile(String accessToken);

}
