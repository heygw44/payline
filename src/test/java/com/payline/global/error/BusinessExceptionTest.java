package com.payline.global.error;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BusinessExceptionTest {

    @Test
    @DisplayName("BusinessException은 ErrorCode의 message를 예외 메시지로 사용한다")
    void shouldUseErrorCodeMessage() {
        BusinessException exception = new BusinessException(ErrorCode.OWNER_NOT_FOUND);

        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 업주입니다.");
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.OWNER_NOT_FOUND);
    }

    @Test
    @DisplayName("BusinessException은 RuntimeException을 상속한다")
    void shouldExtendRuntimeException() {
        BusinessException exception = new BusinessException(ErrorCode.INTERNAL_ERROR);

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
