package com.login.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NaverProfile {

    private String id;
    private String email;

}
