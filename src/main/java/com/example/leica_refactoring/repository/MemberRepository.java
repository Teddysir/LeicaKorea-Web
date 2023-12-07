package com.example.leica_refactoring.repository;

import com.example.leica_refactoring.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByMemberId(String memberId);

    boolean existsByMemberId(String memberId);

}
