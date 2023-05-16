package com.dbl.nsl.productcrud;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ProductCrudApplication {

	public static long appStartTime;
	
	public static void main(String[] args) {
		SpringApplication.run(ProductCrudApplication.class, args);
		appStartTime = System.currentTimeMillis();
	}
	
	@GetMapping("/")
	public String index() {
		return "PRODUCT-CRUD APPLICATION RUNNING SINCE : " +  new Date(appStartTime);
	}

}
