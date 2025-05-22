package com.example.security.conf.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys; // Added
import java.nio.charset.StandardCharsets; // Added
import javax.crypto.SecretKey; // Added

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	public JWTAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			chain.doFilter(req, res);
			return;
		}
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token != null) {
			SecretKey key = Keys.hmacShaKeyFor("ThisIsA32ByteSecretKeyForHS256validation!".getBytes(StandardCharsets.UTF_8));
			
			try {
				Claims body = Jwts.parser() // Corrected: Jwts.parser() returns JwtParserBuilder
							.setSigningKey(key)
							.build()
							.parseClaimsJws(token.replace("Bearer ", ""))
							.getBody();
				
				String user = body.getSubject();

				if (user != null) {
					Object authoritiesObj = body.get("Authorities");
					Collection<SimpleGrantedAuthority> authorities = null;
					if (authoritiesObj instanceof String) {
						authorities = Arrays.stream(((String) authoritiesObj).split(","))
								.map(SimpleGrantedAuthority::new)
								.collect(Collectors.toList());
					} else if (authoritiesObj instanceof Collection) {
						authorities = ((Collection<?>) authoritiesObj).stream()
								.map(Object::toString)
								.map(SimpleGrantedAuthority::new)
								.collect(Collectors.toList());
					}
					
					if (authorities == null) {
						authorities = Arrays.asList(); 
					}

					return new UsernamePasswordAuthenticationToken(user, null, authorities);
				}
			} catch (Exception e) {
				SecurityContextHolder.clearContext(); 
				return null;
			}
			return null;
		}
		return null;
	}
}
