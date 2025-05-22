package com.example.security.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // For addFilterBefore if needed

import com.example.security.conf.jwt.JWTAuthenticationFilter;
import com.example.security.conf.jwt.JWTAuthorizationFilter;
import org.springframework.security.config.Customizer; // For cors

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true) // Optional: if you use method security
public class WebSecurity {

	// MyAuthenticationProvider is already a @Component, so it will be picked up.
	// UserDetailsService is also injected into MyAuthenticationProvider.

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		http
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.cors(Customizer.withDefaults()) // Uses a CorsConfigurationSource bean if defined, otherwise defaults
			.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // For H2 console
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/login", "/h2/**", "/api/hello").permitAll()
				.anyRequest().authenticated()
			)
			// JWTAuthenticationFilter needs AuthenticationManager
			.addFilter(new JWTAuthenticationFilter(authenticationManager))
			// JWTAuthorizationFilter also needs AuthenticationManager (its superclass BasicAuthenticationFilter does)
			.addFilterBefore(new JWTAuthorizationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}

}
