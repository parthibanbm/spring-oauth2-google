package com.parthi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.csrf(csrf-> csrf.disable())
	        	.authorizeHttpRequests(auth-> auth.anyRequest().authenticated())	//Every request to be authenticated               
	                //.oauth2Login(Customizer.withDefaults()); // what Authentication mechanism is oAuthlogin
	        	.oauth2Login(oauth2 -> oauth2.loginPage("/oauth2/authorization/google"));
	        return http.build();
	    }

}
