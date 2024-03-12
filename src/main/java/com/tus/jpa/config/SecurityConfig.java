package com.tus.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Import(PasswordEncoderConfig.class)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return  provider;
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/","/serviceLayer/**",  "/css/**", "/js/**", "/images/**", "/user/register","/user/login","/wines/**","/uploadImage","/customers/**","/customers/wines/**", "/cart/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN") 
         //       .antMatchers("/customers/wines/{id}/rating").hasRole("CUSTOMER") // Allow only authenticated users to access this endpoint
                .antMatchers(HttpMethod.POST, "/login").permitAll() // Allow only POST requests for login
                .anyRequest().authenticated()
            .and()
            	.formLogin()
			            .loginPage("/user/login")
			            .permitAll()
			            .loginProcessingUrl("/login") // Change this line
			            .defaultSuccessUrl("/admin/dashboard", true)
			            .failureUrl("/login?error")
			            .permitAll()
		            .and()
		                .logout()
		                .permitAll();
    }
}