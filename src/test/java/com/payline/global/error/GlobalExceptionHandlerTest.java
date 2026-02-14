package com.payline.global.error;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("BusinessException 발생 시 ErrorCode에 맞는 응답을 반환한다")
    void shouldHandleBusinessException() throws Exception {
        mockMvc.perform(get("/test/business-exception"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("존재하지 않는 업주입니다."))
                .andExpect(jsonPath("$.errorCode").value("O001"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("AccessDeniedException 발생 시 403 응답을 반환한다")
    void shouldHandleAccessDeniedException() throws Exception {
        mockMvc.perform(get("/test/access-denied"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("A002"));
    }

    @Test
    @DisplayName("AuthenticationException 발생 시 401 응답을 반환한다")
    void shouldHandleAuthenticationException() throws Exception {
        mockMvc.perform(get("/test/authentication-exception"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("A001"));
    }

    @Test
    @DisplayName("예상치 못한 Exception 발생 시 500 응답을 반환한다")
    void shouldHandleUnexpectedException() throws Exception {
        mockMvc.perform(get("/test/unexpected-exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("C002"));
    }

    @RestController
    static class TestController {

        @GetMapping("/test/business-exception")
        public void businessException() {
            throw new BusinessException(ErrorCode.OWNER_NOT_FOUND);
        }

        @GetMapping("/test/access-denied")
        public void accessDenied() {
            throw new AccessDeniedException("접근 거부");
        }

        @GetMapping("/test/authentication-exception")
        public void authenticationException() {
            throw new BadCredentialsException("인증 실패");
        }

        @GetMapping("/test/unexpected-exception")
        public void unexpectedException() {
            throw new RuntimeException("예상치 못한 오류");
        }
    }
}
