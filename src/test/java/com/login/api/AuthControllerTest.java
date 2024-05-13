package com.login.api;

import com.login.enumeration.LoginType;
import com.login.request.AuthRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("소셜 로그인 회원 가입을 요청하고 정상 응답한다.")
    void signup() throws Exception {
        // given
        AuthRequest request = AuthRequest.builder()
                .socialId("naver12345")
                .type(LoginType.NAVER.getId())
                .build();

        // when, then
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("소셜 로그인 회원 가입을 요청하지만 소셜 ID 값이 없으면 에러를 응답한다.")
    void signupNotExistSocialId() throws Exception {
        // given
        AuthRequest request = AuthRequest.builder()
                .type(LoginType.NAVER.getId())
                .build();

        // when, then
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("소셜 로그인 회원 가입을 요청하지만 소셜 ID 값이 공백이면 에러를 응답한다.")
    void signupBlankSocialId() throws Exception {
        // given
        AuthRequest request = AuthRequest.builder()
                .socialId("     ")
                .type(LoginType.NAVER.getId())
                .build();

        // when, then
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("소셜 로그인 회원 가입을 요청하지만 로그인 타입 값이 없으면 에러를 응답한다.")
    void signupNotExistType() throws Exception {
        // given
        AuthRequest request = AuthRequest.builder()
                .socialId("kakao12345")
                .build();

        // when, then
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("소셜 로그인 회원 가입을 요청하지만 로그인 타입 값이 유효하지 않다면 에러를 응답한다.")
    void signupInvalidType() throws Exception {
        // given
        AuthRequest request = AuthRequest.builder()
                .socialId("kakao12345")
                .type((short) 5)
                .build();

        // when, then
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
