package com.example.leica_refactoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
       http.authorizeRequests()
               .antMatchers("/","/login","/create/**", "/delete/**","/find/**","/search/**","/category/**","/send").permitAll()
               .antMatchers("/post","/upload").authenticated()
               .anyRequest().authenticated()
               .and()
               .csrf().disable()
               .httpBasic().and()
               .cors()
               .and()
               .formLogin()
               .and()
               .sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .sessionFixation()
               .none();
        return http.build();
    }
}
