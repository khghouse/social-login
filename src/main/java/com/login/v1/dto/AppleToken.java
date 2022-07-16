package com.login.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class AppleToken {

	@Getter
	@Builder
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Request {
		private String code;
		private String client_id;
		private String client_secret;
		private String grant_type;
		private String refresh_token;
	}

	@Getter
	@ToString
	public static class Response {
		private String access_token;
		private String expires_in;
		private String id_token;
		private String refresh_token;
		private String token_type;
		private String error;
	}
}
