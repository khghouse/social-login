package com.login.v1.service;

import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class LoginService {

	@Value("${apple.client.id}")
	private String appleClientId;

	@Value("${apple.team.id}")
	private String appleTeamId;

	@Value("${apple.key.id}")
	private String appleKeyId;

	public String makeClientSecret() throws Exception {
		Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());

		return Jwts.builder()
				.setHeaderParam("alg", "ES256")
				.setHeaderParam("kid", appleKeyId)
				.setIssuer(appleTeamId)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expirationDate)
				.setAudience("https://appleid.apple.com")
				.setSubject(appleClientId)
				.signWith(SignatureAlgorithm.ES256, this.getPrivateKey())
				.compact();
	}

	private PrivateKey getPrivateKey() throws Exception {
		ClassPathResource resource = new ClassPathResource("AuthKey.p8");
		String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));
		Reader pemReader = new StringReader(privateKey);
		PEMParser pemParser = new PEMParser(pemReader);
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
		PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
		return converter.getPrivateKey(object);
	}
}
