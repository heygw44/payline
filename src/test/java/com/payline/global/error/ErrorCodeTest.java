package com.payline.global.error;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ErrorCodeTest {

    @ParameterizedTest
    @EnumSource(ErrorCode.class)
    @DisplayName("모든 ErrorCode는 code, message, httpStatus를 가진다")
    void allErrorCodesShouldHaveRequiredFields(ErrorCode errorCode) {
        assertThat(errorCode.getCode()).isNotBlank();
        assertThat(errorCode.getMessage()).isNotBlank();
        assertThat(errorCode.getHttpStatus()).isBetween(400, 599);
    }

    @Test
    @DisplayName("모든 ErrorCode의 code는 고유하다")
    void allErrorCodesShouldHaveUniqueCode() {
        Set<String> codes = Arrays.stream(ErrorCode.values())
                .map(ErrorCode::getCode)
                .collect(Collectors.toSet());

        assertThat(codes).hasSameSizeAs(ErrorCode.values());
    }

    @Test
    @DisplayName("ErrorCode는 20개가 정의되어 있다")
    void shouldHave20ErrorCodes() {
        assertThat(ErrorCode.values()).hasSize(20);
    }
}
