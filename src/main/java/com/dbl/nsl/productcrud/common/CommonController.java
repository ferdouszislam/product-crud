package com.dbl.nsl.productcrud.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commons")
public class CommonController {

	@Autowired
	private ApiKeyHelper apiKeyHelper;
	
	@RequestMapping(value = "/apiKey", method = RequestMethod.GET)
	public ResponseEntity<?> getApiKey(@RequestParam String username) {
		Map<String, String> data = new HashMap<>();
		data.put("apiKey", apiKeyHelper.getApiKeyForUsername(username));
		return ResponseEntity.ok(data);
	}
	
}
