package com.example.leica_refactoring.entity;

import lombok.Getter;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@Getter
@MappedSuperclass // 실제로 테이블로 매핑은 안되지만 다른 클래스에서 상속받아 사용할 수 있음
@EntityListeners(AuditingEntityListener.class) // 엔티티가 실제
public abstract class PostTime {

    @CreatedDate
    @Column(name = "create_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modified_at;

}
