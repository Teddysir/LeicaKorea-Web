package com.example.leica_refactoring.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@NonNull
@RequiredArgsConstructor
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://krleicablog.shop:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS","HEAD")
                .allowedHeaders("Authorization", "Content-Type")
                .exposedHeaders("userCode", "loginType","authorization","Cookie")
                .allowCredentials(true)
                .maxAge(3600);
    }
}