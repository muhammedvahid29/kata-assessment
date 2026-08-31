package com.bookstore.onlinebookstore.securirty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bookstore.onlinebookstore.auth.service.CustomUserDetailsService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SecurityConfig {
	CustomUserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> 
				session.sessionCreationPolicy(
					SessionCreationPolicy.STATELESS
				)
			)
			
			.headers(headers ->
	            headers.frameOptions(frame ->
	                frame.sameOrigin()
	            )
			)	
			
			.userDetailsService(userDetailsService)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/api/auth/register"	
				).permitAll()
				
				.requestMatchers(
					"/h2-console/**"
				).permitAll()
				
				.requestMatchers(
						"/api/inventory/**"	
				).hasRole("ADMIN")
				
				.requestMatchers(
						"/api/cart/**"	
				).hasRole("CUSTOMER")
				
				.requestMatchers(
						"/api/orders/**"	
				).hasRole("CUSTOMER")
				
				.anyRequest().authenticated()
			)
			.httpBasic(basic -> {});
		
		return http.build();
	}
}
