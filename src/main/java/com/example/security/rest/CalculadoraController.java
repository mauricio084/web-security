package com.example.security.rest;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.annotations.isAdmin;

@RestController
@RequestMapping("calculadora")
public class CalculadoraController {

	@isAdmin
	@GetMapping("sum")
	public double sum(@RequestParam("a") double a, @RequestParam("b") double b) {
		return a + b;
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("substract")
	public double substract(@RequestParam("a") double a, @RequestParam("b") double b) {
		return a - b;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') and #b != 0")
	@PostAuthorize("returnObject ge 1")
	@GetMapping("divide")
	public double divide(@RequestParam("a") double a, 
			@RequestParam("b") double b) {
		return a / b;
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("multiply")
	public double multiply(@RequestParam("a") double a, @RequestParam("b") double b) {
		return a * b;
	}
}
