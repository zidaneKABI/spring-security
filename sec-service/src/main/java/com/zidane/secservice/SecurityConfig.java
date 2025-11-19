package com.zidane.secservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/users/**").permitAll()
                    .requestMatchers("/roles").permitAll()
                    .requestMatchers("addroletouser").permitAll()
                    .requestMatchers("/avis/**").permitAll()
                    .requestMatchers("/inscription").permitAll()
                    .requestMatchers("/activation").permitAll()
                    .requestMatchers("/desactivation").permitAll()
                    
                   .anyRequest().authenticated()).formLogin()
                ; 

        return http.build();
    }

    
}



