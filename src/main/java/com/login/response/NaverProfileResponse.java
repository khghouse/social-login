package com.login.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NaverProfileResponse {

    private String resultcode;
    private String message;
    private NaverProfile response;

}
