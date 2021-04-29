package com.example.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.security.model.Role;
import com.example.security.model.User;
import com.example.security.repositories.RoleRepository;
import com.example.security.repositories.UserRepository;

@Configuration
class LoadDataBase {

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		return args -> {
			
			Role adminRole = new Role();
			adminRole.setName("ADMIN");
			roleRepository.save(adminRole);
			
			Role viewerRole = new Role();
			viewerRole.setName("VIEWER");
			roleRepository.save(viewerRole);
			
			Role customerRole = new Role();
			customerRole.setName("CUSTOMER");
			roleRepository.save(customerRole);

			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(bCryptPasswordEncoder.encode("12345"));
			admin.addRole(adminRole);
			userRepository.save(admin);
			
			User visitor = new User();
			visitor.setUsername("visitor");
			visitor.setPassword(bCryptPasswordEncoder.encode("12345"));
			visitor.addRole(viewerRole);
			userRepository.save(visitor);
			
			User customer = new User();
			customer.setUsername("customer");
			customer.setPassword(bCryptPasswordEncoder.encode("12345"));
			customer.addRole(customerRole);
			userRepository.save(customer);
		};
	}
}
