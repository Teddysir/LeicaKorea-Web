package com.example.leica_refactoring.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi postApi() {
        Info info = new Info().title("게시물 생성 및 s3 업로드 관련 API").version("v0.1");
        String[] paths = {"/post/**","/upload"};

        return GroupedOpenApi.builder()
                .group("post")
                .pathsToMatch(paths)
                .displayName("Post's API")
                .addOpenApiCustomiser(api -> api.setInfo(info))
                .build();
    }

    @Bean
    public GroupedOpenApi categoryApi() {
        Info info = new Info().title("카테고리 관련 API").version("v0.1");
        String[] paths = {"/category/**"};

        return GroupedOpenApi.builder()
                .group("category")
                .pathsToMatch(paths)
                .displayName("Category's API")
                .addOpenApiCustomiser(api -> api.setInfo(info))
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        Info info = new Info().title("이메일 전송, 검색 API").version("v0.1");
        String[] paths = {"/mail","/search","/login","/signup"};

        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch(paths)
                .displayName("User's API")
                .addOpenApiCustomiser(api -> api.setInfo(info))
                .build();
    }
}
