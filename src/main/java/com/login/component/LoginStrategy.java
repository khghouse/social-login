package com.login.component;

import com.login.enumeration.LoginType;

public interface LoginStrategy<T, P> {

    String SPACE = " ";
    String TOKEN_TYPE_BEARER = "Bearer" + SPACE;

    LoginType getLoginType();

    String loginUrl();

    T authentication(String code, String state);

    T authentication(String refreshToken);

    void disconnect(String refreshToken);

    P profile(String accessToken);

}
