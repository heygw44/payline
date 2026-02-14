package com.payline.global.common.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        ApiResponse<Void> response = ApiResponse.error("존재하지 않는 업주입니다.", "OWNER_NOT_FOUND");

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.message()).isEqualTo("존재하지 않는 업주입니다.");
        assertThat(response.errorCode()).isEqualTo("OWNER_NOT_FOUND");
    }

    @Test
    @DisplayName("성공 응답에 message/errorCode를 넣으면 예외가 발생한다")
    void shouldThrowWhenSuccessResponseContainsErrorFields() {
        assertThatThrownBy(() -> new ApiResponse<>(true, "data", "message", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Success response");
    }

    @Test
    @DisplayName("에러 응답에 message 또는 errorCode가 비어 있으면 예외가 발생한다")
    void shouldThrowWhenErrorResponseMissesRequiredFields() {
        assertThatThrownBy(() -> ApiResponse.error("", "OWNER_NOT_FOUND"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("message");

        assertThatThrownBy(() -> ApiResponse.error("존재하지 않는 업주입니다.", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("errorCode");
    }
}
