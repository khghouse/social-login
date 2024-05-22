package com.login.enumeration;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum LoginType {

    EMAIL((short) 1, "이메일 로그인"),
    NAVER((short) 2, "네이버 로그인"),
    KAKAO((short) 3, "카카오 로그인"),
    GOOGLE((short) 4, "구글 로그인");

    private final Short id;
    private final String name;

    private static final Map<Short, LoginType> LOGIN_TYPE_MAP = Collections.unmodifiableMap(Stream.of(values())
            .collect(Collectors.toMap(LoginType::getId, Function.identity())));

    LoginType(Short id, String name) {
        this.id = id;
        this.name = name;
    }

    public static LoginType of(Short type) {
        return Optional.ofNullable(LOGIN_TYPE_MAP.get(type))
                .orElse(null);
    }

}
