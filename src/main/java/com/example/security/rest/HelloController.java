package com.example.security.rest;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class HelloController {

	@GetMapping("hello")
	public String sayHello(){
		return "Hola mundo";
	}
	
	@GetMapping("hello/{nombre}")
	public String sayHelloRecargado(@PathVariable("nombre") String name, Principal principal){
//		Opción 2 para obtener el principal. la opción 1 es con el parametro principal
//		SecurityContext context = SecurityContextHolder.getContext();
//		Authentication authentication = context.getAuthentication();
		
		String user = (String) ((Authentication)principal).getPrincipal();
		return "Feliz dia " + name + " le desea " + user;
	}
}
