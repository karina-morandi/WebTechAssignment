package com.tus.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder());
        return  provider;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/css/**", "/js/**", "/images/**", "/user/register","/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/user/login").permitAll() // Allow only POST requests for login
                .antMatchers("/admin/**").hasRole("ADMIN") 
                .anyRequest().authenticated()
            .and()
            	.formLogin()
			            .loginPage("/user/login")
			            .permitAll()
			            .loginProcessingUrl("/user/login") // Add this line
			            .defaultSuccessUrl("/admin/dashboard", true)
			            .failureUrl("/login?error")
			            .permitAll()
		            .and()
		                .logout()
		                .permitAll();
    }
    
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {  	
//        http.csrf().disable()
//            .authorizeRequests()
//                .antMatchers("/", "/css/**", "/js/**", "../static/**","/src/main/resources/**", "/images/**", "/user/register").permitAll()
//                //.antMatchers("/admin").hasRole("admin") // Redirect based on role
//                .anyRequest().authenticated()
//            .and()
//        .formLogin()
//            .loginPage("/login")
//            .permitAll()
//            .and()
//        .logout()
//            .permitAll();
//        return http.build();
//    }
//    @Bean
//    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeRequests()
//                .antMatchers("/", "/uploadImage","/wines/" ,"/wines/**","/login","/login/**", "/user/register", "/css/**", "/js/**", "../static/**","/src/main/resources/**", "/images/**").permitAll()
//                .antMatchers("/admin").hasRole("admin") // Redirect based on role
//                .anyRequest().authenticated();
////                .and()
////            .formLogin()
////                .loginPage("/login")
////                .defaultSuccessUrl("/", true)
////                .permitAll()
////                .and()
////            .logout();
////                .permitAll();
//        return http.build();
//    }
}
