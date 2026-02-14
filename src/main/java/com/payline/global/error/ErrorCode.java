package com.payline.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통
    INVALID_INPUT("C001", "입력값이 올바르지 않습니다.", 400),
    INTERNAL_ERROR("C002", "서버 내부 오류가 발생했습니다.", 500),

    // 인증/인가
    UNAUTHORIZED("A001", "로그인이 필요합니다.", 401),
    ACCESS_DENIED("A002", "접근 권한이 없습니다.", 403),
    LOGIN_FAILED("A003", "이메일 또는 비밀번호가 올바르지 않습니다.", 401),

    // 회원
    MEMBER_NOT_FOUND("M001", "존재하지 않는 회원입니다.", 404),
    DUPLICATE_EMAIL("M002", "이미 사용 중인 이메일입니다.", 409),
    LAST_ADMIN("M003", "최소 1명의 관리자가 존재해야 합니다.", 400),

    // 업주
    OWNER_NOT_FOUND("O001", "존재하지 않는 업주입니다.", 404),
    DUPLICATE_BUSINESS_NUMBER("O002", "이미 등록된 사업자번호입니다.", 409),
    OWNER_HAS_ORDERS("O003", "주문이 존재하는 업주는 삭제할 수 없습니다.", 400),

    // 주문
    ORDER_NOT_FOUND("OR001", "존재하지 않는 주문입니다.", 404),
    AMOUNT_MISMATCH("OR002", "주문상세 합계가 총 주문 금액과 일치하지 않습니다.", 400),
    EMPTY_ORDER_DETAIL("OR003", "최소 1개의 주문상세가 필요합니다.", 400),

    // 보상금
    REWARD_NOT_FOUND("R001", "존재하지 않는 보상 내역입니다.", 404),
    REWARD_ALREADY_SETTLED("R002", "이미 정산에 포함된 보상금은 수정/삭제할 수 없습니다.", 400),

    // 정산
    SETTLE_NOT_FOUND("S001", "존재하지 않는 지급 내역입니다.", 404),
    DUPLICATE_SETTLE("S002", "동일 업주/기간의 정산이 이미 존재합니다.", 409),
    INVALID_STATE_TRANSITION("S003", "허용되지 않는 상태 전이입니다.", 400),
    SETTLE_NOT_DELETABLE("S004", "대기 상태의 지급만 삭제할 수 있습니다.", 400);

    private final String code;
    private final String message;
    private final int httpStatus;
}
