package com.example.leica_refactoring.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@NonNull
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final Environment env;
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        String[] activeProfiles = env.getActiveProfiles();

        String[] allowedOrigins;

        List<String> allowedOriginsProfiles = Arrays.asList("local", "dev", "staging");

        if (allowedOriginsProfiles.contains(activeProfiles[0])) {
            allowedOrigins = new String[]{"*", "*:*"};
        } else {
            allowedOrigins = new String[]{"*", "*:*"};
        }

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
//                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("Content-Type", "Authorization", "cache-control");

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .exposedHeaders("userCode", "loginType")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true);
    }
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000", "http://krleicablog.shop:8080")
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowedHeaders("Authorization", "Content-Type")
//                .exposedHeaders("userCode", "loginType","authorization","Cookie")
//                .allowCredentials(true)
//                .maxAge(3600);
//    }
}