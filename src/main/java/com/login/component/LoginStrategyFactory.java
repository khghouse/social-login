package com.login.component;

import com.login.enumeration.LoginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LoginStrategyFactory {

    private final Map<LoginType, LoginStrategy> strategyMap;

    @Autowired
    public LoginStrategyFactory(List<LoginStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(LoginStrategy::getLoginType, Function.identity()));
    }

    public LoginStrategy getStrategy(LoginType loginType) {
        return strategyMap.get(loginType);
    }

}
