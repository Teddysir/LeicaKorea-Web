package com.example.leica_refactoring.service.jwt;

import com.example.leica_refactoring.error.exception.requestError.BadRequestException;
import com.example.leica_refactoring.error.security.ErrorCode;
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

        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            String memberId = operations.get(token);

            if (memberId != null) {
                return memberId;
            }

        } catch (NullPointerException e) {
            throw new BadRequestException("400", ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        return null;
    }

    public void delValues(String token) {
        redisTemplate.delete(token);
    }
}
