package com.example.security.conf;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.security.model.Role;
import com.example.security.model.User;
import com.example.security.repositories.UserRepository;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Transactional
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        User usuario = userRepository.findByUsername(username);
        
        if (usuario == null) {
			throw new UsernameNotFoundException(username);
		}
        
        if (encoder.matches(password, usuario.getPassword()) && usuario.isActivo()){
        	
        	List<SimpleGrantedAuthority> roles = getRoles(usuario);

    		// Crea el objeto principal
			org.springframework.security.core.userdetails.User principal = 
					new org.springframework.security.core.userdetails.User(
							usuario.getUsername(), 
							usuario.getPassword(), 
							roles);
    				
        	return new UsernamePasswordAuthenticationToken(principal, null, roles);
        }
        
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	private List<SimpleGrantedAuthority> getRoles(User usuario) {
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		
		for (Role role : usuario.getRoles()) {
			roles.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
		}
		
		return roles;
	}

}
