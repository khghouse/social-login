package com.login;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SocialLoginApplication {

	@Bean
	public RestTemplate restClient() {
		// 타임아웃 설정
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectTimeout(5 * 1000); // 5 sec
		httpRequestFactory.setReadTimeout(60 * 1000); // 60 sec

		HttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(1000).setMaxConnPerRoute(1000).build();
		httpRequestFactory.setHttpClient(httpClient);

		return new RestTemplate(httpRequestFactory);
	}

	public static void main(final String[] args) {
		SpringApplication.run(SocialLoginApplication.class, args);
	}
}
