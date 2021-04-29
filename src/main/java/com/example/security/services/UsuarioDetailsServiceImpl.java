package com.example.security.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.security.model.Role;
import com.example.security.model.User;
import com.example.security.repositories.UserRepository;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {
	private UserRepository usuarioRepository;

	public UsuarioDetailsServiceImpl(UserRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User usuario = usuarioRepository.findByUsername(username);

		if (usuario == null) {
			throw new UsernameNotFoundException(username);
		}
		
		List<SimpleGrantedAuthority> roles = getRoles(usuario);

		return new org.springframework.security.core.userdetails.User
				(usuario.getUsername(), usuario.getPassword(), roles);
	}

	private List<SimpleGrantedAuthority> getRoles(User usuario) {
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		
		for (Role role : usuario.getRoles()) {
			roles.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
		}
		
		return roles;
	}
}
