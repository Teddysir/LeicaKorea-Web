package com.example.leica_refactoring.service.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    public void setValues(String token, String memberId) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue(); // opsForValue는 String 타입
//        Map<String, String> map = new HashMap<>();
//        map.put("memberId", memberId);
        if (this.getValues(token) != null) {
            this.delValues(token);
        }
        operations.set(token, memberId, Duration.ofDays(3)); // 3일 뒤 삭제
    }

    public String getValues(String token) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String memberId = operations.get(token);

        if (memberId != null) {
            return memberId;
        }

        return null;
    }

    public void delValues(String token) {
        redisTemplate.delete(token);
    }
}
