package com.example.leica_refactoring.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    public void setValues(String token, String memberId) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue(); // opsForValue는 String 타입
        Map<String, String> map = new HashMap<>();
        map.put("memberId", memberId);
        operations.set(token, map, Duration.ofDays(3)); // 3일 뒤 삭제
    }

    public Map<String, String> getValues(String token) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        Object object = operations.get(token);
        if (object != null && object instanceof Map) {
            return (Map<String, String>) object;
        }
        return null;
    }

    public void delValues(String token) {
        redisTemplate.delete(token);
    }
}
