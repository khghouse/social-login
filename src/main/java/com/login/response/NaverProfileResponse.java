package com.login.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverProfileResponse {

    private String resultcode;
    private String message;
    private NaverProfile response;

}
