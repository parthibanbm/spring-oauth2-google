package com.parthi.controller;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.proc.SecurityContext;

@RestController
public class Controller {
	
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
	
	@GetMapping("/hi")
	public String hi() {
		return "hi world!";
	}
	
	
	@GetMapping("/hey")
	public String hey() {
		return "hey";
	}
	
	
	  @GetMapping("/me1") public Map<String, Object>
	  getUser(@AuthenticationPrincipal OAuth2User user) {
	  
	  Map<String, Object> response = new HashMap<>(); response.put("name",
	  user.getAttribute("name")); response.put("email",
	  user.getAttribute("email")); response.put("picture",
	  user.getAttribute("picture"));
	  
	  return response;
	  
	  }
	 
	
	@GetMapping("/me2")
	public Map<String, Object> getUser() {		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
		OAuth2User user = (OAuth2User) authentication.getPrincipal();		
		
		Map<String, Object> response = new HashMap<>();
		response.put("name", user.getAttribute("name"));
		response.put("email", user.getAttribute("email"));
		response.put("picture", user.getAttribute("picture"));
		
		return response;
		
	}
	
}
