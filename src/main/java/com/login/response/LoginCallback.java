package com.login.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 네이버, 카카오 로그인 후, code, state를 응답 받기 위한 dto
 * - 네이버 : code, state
 * - 카카오 : code, state, error, error_description
 */
@Getter
@Setter
public class LoginCallback {

    private String code;
    private String state;
    private String error;
    private String error_description;

}
