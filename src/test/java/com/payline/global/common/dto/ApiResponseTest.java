package com.payline.global.common.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @Test
    @DisplayName("success()는 성공 응답을 반환한다")
    void shouldReturnSuccessResponse() {
        ApiResponse<String> response = ApiResponse.success("data");

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo("data");
        assertThat(response.message()).isNull();
        assertThat(response.errorCode()).isNull();
    }

    @Test
    @DisplayName("success()에 null data를 전달하면 data가 null인 성공 응답을 반환한다")
    void shouldReturnSuccessResponseWithNullData() {
        ApiResponse<Void> response = ApiResponse.success(null);

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isNull();
    }

    @Test
    @DisplayName("error()는 에러 응답을 반환한다")
    void shouldReturnErrorResponse() {
        ApiResponse<?> response = ApiResponse.error("존재하지 않는 업주입니다.", "OWNER_NOT_FOUND");

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("존재하지 않는 업주입니다.");
        assertThat(response.errorCode()).isEqualTo("OWNER_NOT_FOUND");
    }
}
