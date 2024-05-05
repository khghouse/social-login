package com.login.repository;

import com.login.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialIdAndDeletedFalse(String socialId);

    Optional<Member> findByIdAndDeletedFalse(Long id);

}
