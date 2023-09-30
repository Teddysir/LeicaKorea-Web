package com.example.leica_refactoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@Order(Ordered.HIGHEST_PRECEDENCE) // Config 우선진입
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
                    .exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
               .and()
                   .formLogin()
               .and()
                   .sessionManagement()
                   .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                   .sessionFixation()
                   .none();
        return http.build();
    }

}
