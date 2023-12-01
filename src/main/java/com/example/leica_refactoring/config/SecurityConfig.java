package com.example.leica_refactoring.config;

import com.example.leica_refactoring.jwt.JwtAuthenticationTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableWebMvc
@Order(Ordered.HIGHEST_PRECEDENCE) // Config 우선진입
public class SecurityConfig {

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .httpBasic().disable()
                    .cors()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                    .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    // 로그인, 검색, 견적서 요청 api는 모두 사용 가능
                    .antMatchers(HttpMethod.POST, "/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/signup").permitAll()
                    .antMatchers(HttpMethod.GET, "/search").permitAll()
                    .antMatchers(HttpMethod.POST, "/mail").hasAnyAuthority("USER")
                    // 게시물 조회는 모두 사용가능 이외는 모두 유저권한 요구
                    .antMatchers(HttpMethod.GET, "/post/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/post/**").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/post/**").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/post/**").hasAuthority("ADMIN")
                    // s3 이미지 업로드 api는 유저권한 필요함
                    .antMatchers(HttpMethod.POST, "/upload").hasAuthority("ADMIN")
                    // 카테고리 조회는 모두 사용가능 이외는 모두 유저 권한 요구
                    .antMatchers(HttpMethod.GET, "/category/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/category/**").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/category/**").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/category/**").hasAuthority("ADMIN")
                    //나머지 api들은 모두 허가없이 접속 가능
                    .anyRequest().permitAll()
                .and()
                    .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                    .logout()
                    .logoutSuccessUrl("/");
            return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}
