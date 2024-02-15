package com.tus.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider
                 = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder());
        return  provider;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/login","/user/register", "/css/**", "/js/**").permitAll()
                .antMatchers("/admin").hasRole("admin") // Redirect based on role
                .anyRequest().authenticated()
                .and()
            .formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/", true)
//                .permitAll()
                .and()
            .logout();
//                .permitAll();
        return http.build();
    }
}
//



//}



//// Handle redirection to login page
//if (xhr.status == 302 && xhr.getResponseHeader('Location') == '/login') {
//  console.log("Redirected to login page");
//  window.location.href = "/login"; // Redirect to login page
//} else {
//  // Log the error message



//package com.tus.jpa.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//@Override
//protected void configure(HttpSecurity http) throws Exception {
//  http.authorizeRequests()
//          .antMatchers("/","/login" ,"/user/register").permitAll()
//          .antMatchers("/wines").authenticated()
//          .anyRequest().authenticated()
//          .and()
//      .formLogin()
//          .loginPage("/login")
//          .defaultSuccessUrl("/wines", true)
//          .permitAll()
//          .and()
//      .logout()
//          .logoutSuccessUrl("/login?logout")
//          .permitAll();
//}


//@Autowired
//private UserDetailsService userDetailsService;
//






//    @Bean
//    AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider
//                 = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(encoder());
//        return  provider;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {  	
//    	 http.csrf().disable()
//    	 .authorizeRequests()
////         .anyRequest().authenticated()
//    	 .antMatchers("/").authenticated()
//         .antMatchers("/user/createUser")
//         .hasAuthority("ADMIN")
//         .antMatchers("/user/name")
//         .hasAuthority("ADMIN")
//         .and().formLogin()
//         //.and().httpBasic()
//         .and().logout();
//    	 
//        return http.build();
//    }
//    
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeRequests()
//                .antMatchers("/", "/home", "/login", "/register").permitAll() // Allow access to these pages without authentication
//                .antMatchers("/admin").hasAuthority("ADMIN") // Require ADMIN authority for admin page
//                .anyRequest().authenticated() // Require authentication for all other requests
//                .and()
//            .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
//            .logout()
//                .logoutSuccessUrl("/login?logout")
//                .permitAll();
//    }
//
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
//    }
//}