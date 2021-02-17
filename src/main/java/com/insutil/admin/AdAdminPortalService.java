package com.insutil.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AdAdminPortalService {

	public static void main(String[] args) {
		SpringApplication.run(AdAdminPortalService.class, args);
	}

}
