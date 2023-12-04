package com.example.leica_refactoring.member;

import com.example.leica_refactoring.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LoadDatabase {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(MemberRepository repository){
        return args -> {
            log.info("Preloading"+repository.save(new Member(1L, "admin", "admin", passwordEncoder.encode("12345678"))));

        };
    }
}