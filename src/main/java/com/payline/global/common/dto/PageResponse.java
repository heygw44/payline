package com.payline.global.common.dto;

import java.util.List;
import java.util.Objects;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public PageResponse {
        content = List.copyOf(Objects.requireNonNull(content, "content must not be null"));

        if (page < 0) {
            throw new IllegalArgumentException("page must be greater than or equal to 0");
        }
        if (size < 0) {
            throw new IllegalArgumentException("size must be greater than or equal to 0");
        }
        if (totalElements < 0) {
            throw new IllegalArgumentException("totalElements must be greater than or equal to 0");
        }

        int expectedTotalPages = calculateTotalPages(size, totalElements);
        if (totalPages != expectedTotalPages) {
            throw new IllegalArgumentException(
                    "totalPages does not match calculated value: expected=" + expectedTotalPages + ", actual=" + totalPages);
        }
    }

    public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = calculateTotalPages(size, totalElements);
        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

    private static int calculateTotalPages(int size, long totalElements) {
        if (size < 0) {
            throw new IllegalArgumentException("size must be greater than or equal to 0");
        }
        if (totalElements < 0) {
            throw new IllegalArgumentException("totalElements must be greater than or equal to 0");
        }
        if (size == 0) {
            if (totalElements > 0) {
                throw new IllegalArgumentException("totalElements must be 0 when size is 0");
            }
            return 0;
        }
        if (totalElements == 0) {
            return 0;
        }

        long totalPages = ((totalElements - 1) / size) + 1;
        if (totalPages > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("totalPages exceeds int range: " + totalPages);
        }
        return (int) totalPages;
    }
}
