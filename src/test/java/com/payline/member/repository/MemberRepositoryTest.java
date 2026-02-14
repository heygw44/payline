package com.payline.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.payline.global.config.JpaAuditingConfig;
import com.payline.member.domain.Member;
import com.payline.member.domain.MemberRole;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("save")
    class Save {

        @Test
        @DisplayName("Member 저장 후 id가 자동 생성된다")
        void shouldGenerateIdAfterSave() {
            Member member = new Member("test@email.com", "encodedPassword", "홍길동", MemberRole.USER);

            Member saved = memberRepository.save(member);

            assertThat(saved.getId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("findByEmailIgnoreCaseAndDeletedFalse")
    class FindByEmailIgnoreCaseAndDeletedFalse {

        @Test
        @DisplayName("삭제되지 않은 회원을 이메일로 조회할 수 있다")
        void shouldFindActiveByEmail() {
            Member member = new Member("TeSt@email.com", "encodedPassword", "홍길동", MemberRole.USER);
            memberRepository.save(member);

            Optional<Member> found = memberRepository.findByEmailIgnoreCaseAndDeletedFalse("TEST@email.com");

            assertThat(found).isPresent();
            assertThat(found.get().getEmail()).isEqualTo("test@email.com");
        }

        @Test
        @DisplayName("삭제된 회원은 조회되지 않는다")
        void shouldNotFindDeletedMember() {
            Member member = new Member("test@email.com", "encodedPassword", "홍길동", MemberRole.USER);
            member.softDelete();
            memberRepository.save(member);

            Optional<Member> found = memberRepository.findByEmailIgnoreCaseAndDeletedFalse("test@email.com");

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsByEmailIgnoreCase")
    class ExistsByEmailIgnoreCase {

        @Test
        @DisplayName("활성 상태의 회원 이메일이면 true를 반환한다")
        void shouldReturnTrueForExistingActiveEmail() {
            Member member = new Member("TeSt@email.com", "encodedPassword", "홍길동", MemberRole.USER);
            memberRepository.save(member);

            assertThat(memberRepository.existsByEmailIgnoreCase("TEST@email.com")).isTrue();
        }

        @Test
        @DisplayName("삭제된 회원 이메일이어도 true를 반환한다")
        void shouldReturnTrueForDeletedEmail() {
            Member member = new Member("test@email.com", "encodedPassword", "홍길동", MemberRole.USER);
            member.softDelete();
            memberRepository.save(member);

            assertThat(memberRepository.existsByEmailIgnoreCase("test@email.com")).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 이메일이면 false를 반환한다")
        void shouldReturnFalseForNonExistingEmail() {
            assertThat(memberRepository.existsByEmailIgnoreCase("nonexistent@email.com")).isFalse();
        }
    }

    @Nested
    @DisplayName("countByRoleAndDeletedFalse")
    class CountByRoleAndDeletedFalse {

        @Test
        @DisplayName("활성 ADMIN 수를 정확히 카운트한다")
        void shouldCountActiveAdmins() {
            memberRepository.save(new Member("admin1@email.com", "pw", "관리자1", MemberRole.ADMIN));
            memberRepository.save(new Member("admin2@email.com", "pw", "관리자2", MemberRole.ADMIN));
            memberRepository.save(new Member("user1@email.com", "pw", "사용자1", MemberRole.USER));

            Member deletedAdmin = new Member("admin3@email.com", "pw", "관리자3", MemberRole.ADMIN);
            deletedAdmin.softDelete();
            memberRepository.save(deletedAdmin);

            long count = memberRepository.countByRoleAndDeletedFalse(MemberRole.ADMIN);

            assertThat(count).isEqualTo(2);
        }
    }
}
