package com.source.backend.beans;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringBeans {

    @Bean
    public UserDetailsService userDetailsService(){
        return new com.source.backend.service.UserDetailsServiceImpl();
    }
}
