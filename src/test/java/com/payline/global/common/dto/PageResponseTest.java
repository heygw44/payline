package com.payline.global.common.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PageResponseTest {

    @Test
    @DisplayName("of()는 totalPages를 자동 계산한다")
    void shouldCalculateTotalPages() {
        PageResponse<String> response = PageResponse.of(
                List.of("a", "b", "c"), 0, 20, 150);

        assertThat(response.content()).containsExactly("a", "b", "c");
        assertThat(response.page()).isZero();
        assertThat(response.size()).isEqualTo(20);
        assertThat(response.totalElements()).isEqualTo(150);
        assertThat(response.totalPages()).isEqualTo(8);
    }

    @Test
    @DisplayName("totalElements가 size로 나누어 떨어지면 정확한 페이지 수를 반환한다")
    void shouldCalculateExactTotalPages() {
        PageResponse<String> response = PageResponse.of(List.of("a"), 0, 10, 100);

        assertThat(response.totalPages()).isEqualTo(10);
    }

    @Test
    @DisplayName("빈 content 리스트를 허용한다")
    void shouldAllowEmptyContent() {
        PageResponse<String> response = PageResponse.of(List.of(), 0, 20, 0);

        assertThat(response.content()).isEmpty();
        assertThat(response.totalElements()).isZero();
        assertThat(response.totalPages()).isZero();
    }

    @Test
    @DisplayName("size가 0이면 totalPages는 0이다")
    void shouldReturnZeroTotalPagesWhenSizeIsZero() {
        PageResponse<String> response = PageResponse.of(List.of(), 0, 0, 0);

        assertThat(response.totalPages()).isZero();
    }

    @Test
    @DisplayName("page가 음수면 예외가 발생한다")
    void shouldThrowWhenPageIsNegative() {
        assertThatThrownBy(() -> PageResponse.of(List.of("a"), -1, 10, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("page");
    }

    @Test
    @DisplayName("size가 음수면 예외가 발생한다")
    void shouldThrowWhenSizeIsNegative() {
        assertThatThrownBy(() -> PageResponse.of(List.of("a"), 0, -1, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("size");
    }

    @Test
    @DisplayName("size가 0인데 totalElements가 양수면 예외가 발생한다")
    void shouldThrowWhenSizeIsZeroButTotalElementsIsPositive() {
        assertThatThrownBy(() -> PageResponse.of(List.of("a"), 0, 0, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("totalElements");
    }

    @Test
    @DisplayName("content는 외부에서 수정할 수 없다")
    void shouldReturnImmutableContent() {
        PageResponse<String> response = PageResponse.of(List.of("a", "b"), 0, 10, 2);

        assertThatThrownBy(() -> response.content().add("c"))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
