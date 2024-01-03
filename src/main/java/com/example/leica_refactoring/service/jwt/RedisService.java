package com.example.leica_refactoring.service.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    public void setValues(String memberId, String token) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue(); // opsForValue는 String 타입
        if (this.getValues(token) != null) {
            this.delValues(token);
        }
        operations.set(token, memberId, Duration.ofDays(3)); // 3일 뒤 삭제
    }

    public String getValues(String token) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String memberId = operations.get(token);

        log.info(memberId);
        System.out.println(memberId);

        if (memberId != null) {
            return memberId;
        }
        return null;
    }

    public void delValues(String token) {
        redisTemplate.delete(token);
    }
}
