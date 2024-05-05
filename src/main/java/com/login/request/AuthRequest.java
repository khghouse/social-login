package com.login.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequest {

    @NotBlank(message = "이메일을 입력해 주세요.")
    private String email;

    @NotBlank(message = "SNS 식별값을 입력해 주세요.")
    private String socialId;

    private String refreshToken;

    public AuthServiceRequest toServiceRequest() {
        return AuthServiceRequest.builder()
                .email(email)
                .socialId(socialId)
                .refreshToken(refreshToken)
                .build();
    }

}
