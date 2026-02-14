package com.payline.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberRoleTest {

    @Test
    @DisplayName("MemberRole은 ADMIN, USER 두 값을 가진다")
    void shouldHaveAdminAndUserValues() {
        MemberRole[] values = MemberRole.values();

        assertThat(values).containsExactlyInAnyOrder(MemberRole.ADMIN, MemberRole.USER);
    }

    @Test
    @DisplayName("valueOf로 ADMIN, USER를 조회할 수 있다")
    void shouldResolveByName() {
        assertThat(MemberRole.valueOf("ADMIN")).isEqualTo(MemberRole.ADMIN);
        assertThat(MemberRole.valueOf("USER")).isEqualTo(MemberRole.USER);
    }
}
