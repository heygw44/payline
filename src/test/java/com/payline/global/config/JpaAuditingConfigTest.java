package com.payline.global.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

class JpaAuditingConfigTest {

    private final JpaAuditingConfig config = new JpaAuditingConfig();

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("인증된 사용자면 사용자 이름을 반환한다")
    void shouldReturnUsernameWhenAuthenticated() {
        var auth = new UsernamePasswordAuthenticationToken("admin", null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        AuditorAware<String> auditor = config.auditorAware();

        assertThat(auditor.getCurrentAuditor()).hasValue("admin");
    }

    @Test
    @DisplayName("AnonymousAuthenticationToken이면 SYSTEM을 반환한다")
    void shouldReturnSystemWhenAnonymous() {
        var auth = new AnonymousAuthenticationToken("key", "anonymousUser",
                List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        AuditorAware<String> auditor = config.auditorAware();

        assertThat(auditor.getCurrentAuditor()).hasValue("SYSTEM");
    }

    @Test
    @DisplayName("SecurityContext에 인증 정보가 없으면 SYSTEM을 반환한다")
    void shouldReturnSystemWhenNoAuthentication() {
        SecurityContextHolder.clearContext();

        AuditorAware<String> auditor = config.auditorAware();

        assertThat(auditor.getCurrentAuditor()).hasValue("SYSTEM");
    }
}
