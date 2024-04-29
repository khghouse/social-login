package com.login.api;

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

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("naverLoginUrl", naverLogin.generateLoginUrl());
        return "login";
    }

}
