package com.tus.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//public class Config extends WebMvcConfigurerAdapter{
//	   
//	@Override
//	   public void addResourceHandlers(ResourceHandlerRegistry registry) {  
//	    registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
//	   }
//	}