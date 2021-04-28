package com.example.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.security.model.User;
import com.example.security.repositories.UserRepository;

@Configuration
class LoadDataBase {

	@Bean
	CommandLineRunner initDatabase(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		return args -> {
			User user = new User();
			user.setUsername("pepito");
			user.setPassword(bCryptPasswordEncoder.encode("12345"));
			repository.save(user);
		};
	}
}
