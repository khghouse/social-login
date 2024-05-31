package com.login.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequest {

    @NotBlank(message = "SNS 식별값을 입력해 주세요.")
    private String socialId;

    @NotNull(message = "로그인 타입을 입력해 주세요.")
    @Min(value = 1, message = "로그인 타입은 1,2,3,4 중 하나의 값입니다.")
    @Max(value = 4, message = "로그인 타입은 1,2,3,4 중 하나의 값입니다.")
    private Short type;

    private String email;

    private String refreshToken;

    public AuthServiceRequest toServiceRequest() {
        return AuthServiceRequest.builder()
                .socialId(socialId)
                .type(type)
                .email(email)
                .refreshToken(refreshToken)
                .build();
    }

}
