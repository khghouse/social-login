package com.login.enumeration;

import lombok.Getter;

@Getter
public enum LoginType {

    EMAIL((short) 1, "이메일 로그인"),
    NAVER((short) 2, "네이버 로그인"),
    KAKAO((short) 3, "카카오 로그인");

    private final Short id;
    private final String name;

    LoginType(Short id, String name) {
        this.id = id;
        this.name = name;
    }

}
