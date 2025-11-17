package com.zidane.secservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zidane.secservice.service.AccountService;

@SpringBootApplication
public class SecServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecServiceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner start( AccountService accountService) {
		return args -> {
          //  accountService = new com.zidane.secservice.service.AccountServiceImplementation();
			accountService.AddNewRole(new com.zidane.secservice.sec.entities.AppRole(null, "USER"));
			accountService.AddNewRole(new com.zidane.secservice.sec.entities.AppRole(null, "ADMIN"));
			accountService.AddNewRole(new com.zidane.secservice.sec.entities.AppRole(null, "CUSTOMER_MANAGER"));
			accountService.AddNewRole(new com.zidane.secservice.sec.entities.AppRole(null, "PRODUCT_MANAGER"));
			accountService.AddNewRole(new com.zidane.secservice.sec.entities.AppRole(null, "BILLS_MANAGER"));

			accountService.AddNewUser(new com.zidane.secservice.sec.entities.AppUser(null, "user1", "1234", new java.util.ArrayList<>()));
			accountService.AddNewUser(new com.zidane.secservice.sec.entities.AppUser(null, "admin", "1234", new java.util.ArrayList<>()));
			accountService.AddNewUser(new com.zidane.secservice.sec.entities.AppUser(null, "user2", "1234", new java.util.ArrayList<>()));
			accountService.AddNewUser(new com.zidane.secservice.sec.entities.AppUser(null, "user3", "1234", new java.util.ArrayList<>()));
			accountService.AddNewUser(new com.zidane.secservice.sec.entities.AppUser(null, "user4", "1234", new java.util.ArrayList<>()));
			
			accountService.AddRoleToUser("user1", "USER");
			accountService.AddRoleToUser("admin", "USER");
			accountService.AddRoleToUser("admin", "ADMIN");
			accountService.AddRoleToUser("user2", "USER");
			accountService.AddRoleToUser("user2", "CUSTOMER_MANAGER");
			accountService.AddRoleToUser("user3", "USER");
			accountService.AddRoleToUser("user3", "PRODUCT_MANAGER");
			accountService.AddRoleToUser("user4", "USER");
			accountService.AddRoleToUser("user4", "BILLS_MANAGER");	
			

		};
	}

}
