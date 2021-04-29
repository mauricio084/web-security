package com.example.security;

import java.util.ArrayList;
import java.util.List;

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
			
			Role admin = new Role();
			admin.setName("ADMIN");
			roleRepository.save(admin);
			
			Role customer = new Role();
			customer.setName("USER");
			roleRepository.save(customer);

			List<Role> roles = new ArrayList<>();
			roles.add(admin);
			roles.add(customer);
			
			User user = new User();
			user.setUsername("pepito");
			user.setPassword(bCryptPasswordEncoder.encode("12345"));
			user.setRoles(roles);
			userRepository.save(user);
		};
	}
}
