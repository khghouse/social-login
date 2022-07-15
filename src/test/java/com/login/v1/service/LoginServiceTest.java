package com.login.v1.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoginServiceTest {

	@Autowired
	private LoginService loginService;
	
	@Test
	void test() throws Exception {
		System.out.println(loginService.makeClientSecret());
	}
	
	@Nested
	class LoginApple {
		
		@Test
		void validation() {
			
		}
	}
}
