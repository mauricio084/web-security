package com.example.security.rest;

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
	public String sayHelloRecargado(@PathVariable("nombre") String name){
		return "Hola mundo " + name;
	}
}
