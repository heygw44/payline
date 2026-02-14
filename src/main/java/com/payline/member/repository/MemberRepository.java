package com.payline.member.repository;

import com.payline.member.domain.Member;
import com.payline.member.domain.MemberRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailIgnoreCaseAndDeletedFalse(String email);

    boolean existsByEmailIgnoreCase(String email);

    long countByRoleAndDeletedFalse(MemberRole role);
}
