package com.bookstore.onlinebookstore.security;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bookstore.onlinebookstore.auth.service.CustomUserDetailsService;
import com.bookstore.onlinebookstore.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SecurityConfig {
	CustomUserDetailsService userDetailsService;
	ObjectMapper objectMapper;
	
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
						"/swagger-ui.html",
				        "/swagger-ui/**",
				        "/v3/api-docs",
				        "/v3/api-docs/**"
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
			.exceptionHandling(exception -> exception
		            .authenticationEntryPoint((request, response, authException) -> {

		                response.setStatus(HttpStatus.UNAUTHORIZED.value());
		                response.setContentType("application/json");

		                ErrorResponse error = new ErrorResponse(
		                		LocalDateTime.now().withNano(0),
		                        401,
		                        "UNAUTHORIZED",
		                        "AUthentication required/Bad credentials provided"
		                );

		                response.getWriter().write(
		                        objectMapper.writeValueAsString(error)
		                );
		            })

		            .accessDeniedHandler((request, response, accessDeniedException) -> {

		                response.setStatus(HttpStatus.FORBIDDEN.value());
		                response.setContentType("application/json");

		                ErrorResponse error = new ErrorResponse(
		                		LocalDateTime.now().withNano(0),
		                        403,
		                        "FORBIDDEN",
		                        "You do not have permission to access this resource"
		                );

		                response.getWriter().write(
		                        objectMapper.writeValueAsString(error)
		                );
		            })
		        )
			.httpBasic(basic -> {});
		
		return http.build();
	}
}
