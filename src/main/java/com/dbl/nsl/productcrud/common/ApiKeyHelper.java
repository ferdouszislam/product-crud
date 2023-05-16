package com.dbl.nsl.productcrud.common;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyHelper {

	@Autowired
	private HttpServletRequest request;
	
	private final String SECRET = "nsl@23!";
	
	private final String API_KEY = "apiKey";
	
	private final String ADMIN_CODE = "_guess(me&if%u#can!";
	
	public String getApiKeyForUsername(String username) {
		return EncryptionDecryptionHelper.encrypt(username, SECRET);
	}
	
	public String getUsernameFromHeader() {
		if (isAdmin()) return "ADMIN";
		String apiKey = request.getHeader(API_KEY);
		return Objects.isNull(apiKey) ? null : EncryptionDecryptionHelper.decrypt(apiKey, SECRET);
	}
	
	public boolean isAdmin() {
		return Objects.equals(request.getHeader(API_KEY), ADMIN_CODE);
	}
	
}
