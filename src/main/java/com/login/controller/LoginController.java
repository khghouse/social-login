package com.login.controller;

import com.login.component.KakaoLogin;
import com.login.component.NaverLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final NaverLogin naverLogin;
    private final KakaoLogin kakaoLogin;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("naverLoginUrl", naverLogin.generateLoginUrl());
        model.addAttribute("kakaoLoginUrl", kakaoLogin.generateLoginUrl());
        return "login";
    }

}
