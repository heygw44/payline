# 🧪 테스트 시나리오 명세서: 페이라인 (PayLine)

### 도메인별 테스트 케이스 목록 + 검증 기준

> _PRD v3.0 / 아키텍처 설계서 v1.0 기반_

---

| 항목 | 내용 |
|------|------|
| **문서 유형** | 테스트 시나리오 명세서 |
| **문서 버전** | v1.0 |
| **최종 수정일** | 2026년 2월 12일 |
| **총 테스트 케이스** | 87개 (단위 52 + 통합 25 + 보안 10) |
| **커버리지 목표** | Service 계층 80% 이상 |
| **테스트 프레임워크** | JUnit Jupiter 6.0.2, Mockito 5.20, Spring Boot Test, MockMvc |
| **기술 스택** | Java 21 + Spring Boot 4.0.2 + Vue 3 |

---

## 목차

1. [테스트 전략 개요](#1-테스트-전략-개요)
2. [인증/인가 테스트](#2-인증인가-테스트)
3. [Member 도메인 테스트](#3-member-도메인-테스트)
4. [Owner 도메인 테스트](#4-owner-도메인-테스트)
5. [Order 도메인 테스트](#5-order-도메인-테스트)
6. [Reward 도메인 테스트](#6-reward-도메인-테스트)
7. [Settle 도메인 테스트](#7-settle-도메인-테스트)
8. [보안 테스트](#8-보안-테스트)
9. [테스트 데이터 설계](#9-테스트-데이터-설계)
10. [테스트 실행 가이드](#10-테스트-실행-가이드)

---

## 1. 테스트 전략 개요

### 테스트 피라미드

| 유형 | 도구 | 대상 | 비중 | 목표 |
|------|------|------|------|------|
| **단위 테스트** | JUnit 5 + Mockito | Service, Domain, Calculator | 60% (52건) | 비즈니스 로직 정확성 |
| **통합 테스트** | Spring Boot Test + MockMvc | Controller + Security + DB | 29% (25건) | API 동작 + 인증/인가 |
| **보안 테스트** | Spring Security Test | CSRF, 세션, 권한 | 11% (10건) | 보안 요구사항 충족 |

### 테스트 클래스 네이밍 규칙

| 유형 | 패턴 | 예시 |
|------|------|------|
| 단위 테스트 | `{Class}Test` | `SettleServiceTest`, `SettleStatusTest` |
| 통합 테스트 | `{Class}IntegrationTest` | `SettleControllerIntegrationTest` |
| 보안 테스트 | `{Feature}SecurityTest` | `AuthSecurityTest`, `CsrfSecurityTest` |

### 테스트 어노테이션

| 어노테이션 | 용도 |
|-----------|------|
| `@ExtendWith(MockitoExtension.class)` | 단위 테스트 (Mockito) |
| `@SpringBootTest` + `@AutoConfigureMockMvc` | 통합 테스트 |
| `@DataJpaTest` | JPA Repository 테스트 |
| `@WithMockUser(roles = "ADMIN")` | 인증 사용자 시뮬레이션 |

---

## 2. 인증/인가 테스트

### 2.1 단위 테스트 — CustomUserDetailsService

| TC ID | 테스트 케이스 | 입력 | 기대 결과 | 검증 |
|-------|-------------|------|----------|------|
| AUTH-U-01 | 존재하는 이메일로 사용자 조회 | email: "admin@payline.com" | UserDetails 반환 | email, role 일치 |
| AUTH-U-02 | 존재하지 않는 이메일 | email: "nobody@test.com" | UsernameNotFoundException | 예외 메시지 확인 |
| AUTH-U-03 | 삭제된 회원 조회 | deleted=true인 email | UsernameNotFoundException | 논리 삭제 회원 차단 |

### 2.2 통합 테스트 — 로그인/로그아웃

| TC ID | 테스트 케이스 | Method/URL | 입력 | 기대 결과 |
|-------|-------------|-----------|------|----------|
| AUTH-I-01 | 정상 로그인 | POST /api/login | email + password (form) | 200 + JSESSIONID 쿠키 + JSON |
| AUTH-I-02 | 잘못된 비밀번호 | POST /api/login | email + 틀린 password | 401 + 에러 메시지 |
| AUTH-I-03 | 미등록 이메일 | POST /api/login | 미등록 email | 401 |
| AUTH-I-04 | 로그아웃 | POST /api/logout | (인증된 세션) | 200 + 세션 무효화 |
| AUTH-I-05 | 로그아웃 후 API 접근 | GET /api/members/me | (무효화된 세션) | 401 |
| AUTH-I-06 | 내 정보 조회 | GET /api/members/me | (인증된 세션) | 200 + 사용자 정보 |

---

## 3. Member 도메인 테스트

### 3.1 단위 테스트 — MemberService

| TC ID | 테스트 케이스 | 사전 조건 | 입력 | 기대 결과 |
|-------|-------------|----------|------|----------|
| MBR-U-01 | 정상 회원가입 | — | SignupRequest (유효) | Member 저장, role=USER |
| MBR-U-02 | 이메일 중복 가입 | 동일 이메일 존재 | SignupRequest | DuplicateException (M002) |
| MBR-U-03 | 비밀번호 BCrypt 해싱 | — | SignupRequest | 저장된 password ≠ 원문 |
| MBR-U-04 | 회원 정보 수정 | 회원 존재 | MemberUpdateRequest | name/password 변경 |
| MBR-U-05 | 권한 변경 (USER→ADMIN) | 대상 회원 존재 | role=ADMIN | role 변경 확인 |
| MBR-U-06 | 마지막 관리자 권한 해제 방지 | ADMIN 1명 | role=USER | BusinessException (M003) |
| MBR-U-07 | 회원 soft delete | 회원 존재 | memberId | deleted=true |
| MBR-U-08 | 미존재 회원 조회 | — | 미존재 ID | EntityNotFoundException (M001) |

### 3.2 통합 테스트 — MemberController

| TC ID | 테스트 케이스 | Method/URL | 권한 | 기대 상태코드 |
|-------|-------------|-----------|------|-------------|
| MBR-I-01 | 회원가입 성공 | POST /api/members/signup | 없음 | 201 |
| MBR-I-02 | 이메일 형식 오류 | POST /api/members/signup | 없음 | 400 |
| MBR-I-03 | 관리자 회원 목록 조회 | GET /api/admin/members | ADMIN | 200 |
| MBR-I-04 | 일반회원 관리자 API 접근 | GET /api/admin/members | USER | 403 |

---

## 4. Owner 도메인 테스트

### 4.1 단위 테스트 — OwnerService

| TC ID | 테스트 케이스 | 사전 조건 | 입력 | 기대 결과 |
|-------|-------------|----------|------|----------|
| OWN-U-01 | 정상 업주 등록 | — | OwnerCreateRequest | Owner 저장 |
| OWN-U-02 | 사업자번호 중복 | 동일 번호 존재 | OwnerCreateRequest | DuplicateException (O002) |
| OWN-U-03 | 업주 정보 수정 | 업주 존재 | OwnerUpdateRequest | 필드 변경 확인 |
| OWN-U-04 | 주문 있는 업주 삭제 | 주문 존재 | ownerId | BusinessException (O003) |
| OWN-U-05 | 주문 없는 업주 삭제 | 주문 없음 | ownerId | deleted=true |
| OWN-U-06 | 삭제된 업주 조회 | deleted=true | ownerId | EntityNotFoundException (O001) |
| OWN-U-07 | 업주 상세 (통계) | 관련 데이터 존재 | ownerId | 주문수, 금액, 보상액 확인 |

### 4.2 통합 테스트 — OwnerController

| TC ID | 테스트 케이스 | Method/URL | 권한 | 기대 상태코드 |
|-------|-------------|-----------|------|-------------|
| OWN-I-01 | 업주 등록 성공 | POST /api/owners | ADMIN | 201 |
| OWN-I-02 | 일반회원 업주 등록 | POST /api/owners | USER | 403 |
| OWN-I-03 | 업주 검색 | GET /api/owners?keyword=행복 | USER | 200 |
| OWN-I-04 | 업주별 주문 조회 | GET /api/owners/1/orders | USER | 200 |

---

## 5. Order 도메인 테스트

### 5.1 단위 테스트 — OrderService

| TC ID | 테스트 케이스 | 사전 조건 | 입력 | 기대 결과 |
|-------|-------------|----------|------|----------|
| ORD-U-01 | 정상 주문 등록 | Owner 존재 | Request + details | 저장 |
| ORD-U-02 | OrderDetail 없이 등록 | — | details 빈 리스트 | BusinessException (OR003) |
| ORD-U-03 | OrderDetail 합계 ≠ totalAmount | — | 합계 불일치 | BusinessException (OR002) |
| ORD-U-04 | OrderDetail 합계 = totalAmount | — | 합계 일치 | 정상 저장 |
| ORD-U-05 | 주문 취소 | Order 존재 | status=CANCELLED | 변경 확인 |
| ORD-U-06 | 미존재 Owner로 주문 | Owner 미존재 | ownerId | EntityNotFoundException |
| ORD-U-07 | BigDecimal 정밀도 | — | 33333.33 | 소수점 정확 저장 |

### 5.2 단위 테스트 — Order Entity

| TC ID | 테스트 케이스 | 입력 | 기대 결과 |
|-------|-------------|------|----------|
| ORD-U-08 | validateTotalAmount 성공 | 합계 == totalAmount | 예외 없음 |
| ORD-U-09 | validateTotalAmount 실패 | 합계 ≠ totalAmount | AmountMismatchException |
| ORD-U-10 | isCompleted (COMPLETED) | COMPLETED | true |
| ORD-U-11 | isCompleted (RECEIVED) | RECEIVED | false |

### 5.3 통합 테스트 — OrderController

| TC ID | 테스트 케이스 | Method/URL | 기대 결과 |
|-------|-------------|-----------|----------|
| ORD-I-01 | 주문 등록 성공 | POST /api/orders | 201 |
| ORD-I-02 | 금액 불일치 | POST /api/orders | 400 + OR002 |
| ORD-I-03 | 주문 검색 (기간+상태) | GET /api/orders?... | 200 + 필터링 |

---

## 6. Reward 도메인 테스트

### 6.1 단위 테스트 — RewardService

| TC ID | 테스트 케이스 | 사전 조건 | 입력 | 기대 결과 |
|-------|-------------|----------|------|----------|
| RWD-U-01 | 정상 보상금 등록 | Owner 존재 | RewardCreateRequest | 저장 |
| RWD-U-02 | 보상금 0원 등록 | — | amount=0 | Validation 실패 |
| RWD-U-03 | 미정산 보상금 수정 | settled=false | UpdateRequest | 수정 성공 |
| RWD-U-04 | 정산 포함 보상금 수정 | settled=true | UpdateRequest | BusinessException (R002) |
| RWD-U-05 | 정산 포함 보상금 삭제 | settled=true | rewardId | BusinessException (R002) |
| RWD-U-06 | 미정산 보상금 삭제 | settled=false | rewardId | 정상 삭제 |
| RWD-U-07 | 존재하지 않는 보상금 조회 | — | 미존재 ID | EntityNotFoundException (R001) |

### 6.2 통합 테스트 — RewardController

| TC ID | 테스트 케이스 | Method/URL | 기대 결과 |
|-------|-------------|-----------|----------|
| RWD-I-01 | 보상금 등록 성공 | POST /api/rewards | 201 |
| RWD-I-02 | 사유 상세 10자 미만 | POST /api/rewards | 400 |

---

## 7. Settle 도메인 테스트

### 7.1 단위 테스트 — SettleStatus (상태 전이)

| TC ID | 테스트 케이스 | 현재 상태 | 목표 상태 | 기대 결과 |
|-------|-------------|----------|----------|----------|
| STL-U-01 | PENDING → REQUESTED | PENDING | REQUESTED | 성공 |
| STL-U-02 | REQUESTED → APPROVED | REQUESTED | APPROVED | 성공 |
| STL-U-03 | REQUESTED → REJECTED | REQUESTED | REJECTED | 성공 |
| STL-U-04 | APPROVED → COMPLETED | APPROVED | COMPLETED | 성공 |
| STL-U-05 | PENDING → APPROVED (불가) | PENDING | APPROVED | InvalidStateException |
| STL-U-06 | PENDING → COMPLETED (불가) | PENDING | COMPLETED | InvalidStateException |
| STL-U-07 | COMPLETED → * (불가) | COMPLETED | REQUESTED | InvalidStateException |
| STL-U-08 | REJECTED → * (불가) | REJECTED | REQUESTED | InvalidStateException |

### 7.2 단위 테스트 — SettlementCalculator

| TC ID | 테스트 케이스 | 사전 조건 | 기대 결과 |
|-------|-------------|----------|----------|
| STL-U-09 | 정상 금액 산출 | 주문 3건 + 보상 1건 | totalOrder - totalFee + totalReward |
| STL-U-10 | CANCELLED 주문 제외 | COMPLETED 2건 + CANCELLED 1건 | CANCELLED 미포함 |
| STL-U-11 | RECEIVED 주문 제외 | RECEIVED 1건 + COMPLETED 1건 | RECEIVED 미포함 |
| STL-U-12 | 보상금 없는 경우 | 주문만 존재 | totalRewardAmount = 0 |
| STL-U-13 | 주문 없는 경우 | 보상만 존재 | totalOrderAmount = 0, totalFeeAmount = 0 |
| STL-U-14 | BigDecimal 정밀도 — 수수료 | 33333.33 × 3 | 99999.99 정확 |
| STL-U-15 | BigDecimal 정밀도 — 최종 금액 | 복합 계산 | 산술적 정확 |

### 7.3 단위 테스트 — SettleService

| TC ID | 테스트 케이스 | 사전 조건 | 입력 | 기대 결과 |
|-------|-------------|----------|------|----------|
| STL-U-16 | 정상 지급 생성 | Owner 존재 | SettleCreateRequest | status=PENDING |
| STL-U-17 | 동일 업주/기간 중복 | 동일 Settle 존재 | SettleCreateRequest | DuplicateException (S002) |
| STL-U-17a | 존재하지 않는 지급 조회 | — | 미존재 ID | EntityNotFoundException (S001) |
| STL-U-18 | 존재하지 않는 업주 | Owner 미존재 | ownerId=9999 | EntityNotFoundException (O001) |
| STL-U-19 | startDate > endDate | — | 시작일 > 종료일 | BusinessException |
| STL-U-20 | 생성 시 Reward settled 플래그 변경 | 미정산 보상 3건 | SettleCreateRequest | 3건 모두 settled=true |
| STL-U-21 | 지급 요청 (PENDING→REQUESTED) | status=PENDING | settleId | status=REQUESTED |
| STL-U-22 | 지급 승인 (REQUESTED→APPROVED) | status=REQUESTED | settleId | status=APPROVED |
| STL-U-23 | 지급 완료 (APPROVED→COMPLETED) | status=APPROVED | settleId | status=COMPLETED |
| STL-U-24 | 지급 반려 (REQUESTED→REJECTED) | status=REQUESTED | settleId + reason | status=REJECTED, rejectionReason 저장 |
| STL-U-25 | 반려 시 사유 누락 | status=REQUESTED | reason=null | BusinessException |
| STL-U-26 | 잘못된 상태 전이 (PENDING→APPROVED) | status=PENDING | approve() 호출 | InvalidStateException (S003) |
| STL-U-27 | PENDING 상태 삭제 성공 | status=PENDING | settleId | 정상 삭제 |
| STL-U-28 | REQUESTED 상태 삭제 시도 | status=REQUESTED | settleId | BusinessException (S004) |
| STL-U-29 | COMPLETED 상태 삭제 시도 | status=COMPLETED | settleId | BusinessException (S004) |

### 7.4 통합 테스트 — SettleController

| TC ID | 테스트 케이스 | Method/URL | 권한 | 기대 결과 |
|-------|-------------|-----------|------|----------|
| STL-I-01 | 지급 데이터 생성 | POST /api/settles | ADMIN | 201 + 자동 산출 금액 확인 |
| STL-I-02 | 지급 목록 검색 (상태 필터) | GET /api/settles?status=PENDING | USER | 200 + PENDING만 반환 |
| STL-I-03 | 지급 상세 조회 | GET /api/settles/1 | USER | 200 + 주문/보상 목록 포함 |
| STL-I-04 | 지급 요청 | PATCH /api/settles/1/request | ADMIN | 200 + status=REQUESTED |
| STL-I-05 | 지급 승인 | PATCH /api/settles/1/approve | ADMIN | 200 + status=APPROVED |
| STL-I-06 | 지급 반려 (사유 포함) | PATCH /api/settles/1/reject | ADMIN | 200 + rejectionReason 저장 |
| STL-I-07 | 일반회원 지급 생성 시도 | POST /api/settles | USER | 403 |
| STL-I-08 | 일반회원 상태 변경 시도 | PATCH /api/settles/1/approve | USER | 403 |
| STL-I-09 | 중복 기간 지급 생성 | POST /api/settles | ADMIN | 409 + S002 |

---

## 8. 보안 테스트

### 8.1 CSRF 방어 테스트

| TC ID | 테스트 케이스 | 방법 | 기대 결과 |
|-------|-------------|------|----------|
| SEC-01 | CSRF 토큰 없이 POST 요청 | MockMvc POST /api/owners (토큰 미포함) | 403 Forbidden |
| SEC-02 | CSRF 토큰 포함 POST 요청 | MockMvc POST /api/owners (with csrf()) | 정상 처리 |
| SEC-03 | 잘못된 CSRF 토큰 | MockMvc POST (잘못된 XSRF-TOKEN) | 403 Forbidden |

### 8.2 세션 보안 테스트

| TC ID | 테스트 케이스 | 방법 | 기대 결과 |
|-------|-------------|------|----------|
| SEC-04 | 비인증 사용자 보호 API 접근 | GET /api/owners (세션 없음) | 401 Unauthorized |
| SEC-05 | 로그인 후 세션 ID 변경 확인 | 로그인 전/후 JSESSIONID 비교 | 서로 다른 세션 ID (세션 고정 방어) |
| SEC-06 | 로그아웃 후 세션 무효화 | 로그아웃 후 이전 세션으로 요청 | 401 Unauthorized |
| SEC-06a | 쿠키 HttpOnly 설정 확인 | 로그인 응답의 Set-Cookie 헤더 검사 | JSESSIONID에 HttpOnly 속성 포함 |

### 8.3 권한 제어 테스트

| TC ID | 테스트 케이스 | 방법 | 기대 결과 |
|-------|-------------|------|----------|
| SEC-07 | USER가 ADMIN 전용 API 접근 | @WithMockUser(roles="USER") → POST /api/owners | 403 |
| SEC-08 | USER가 조회 API 접근 | @WithMockUser(roles="USER") → GET /api/owners | 200 |
| SEC-09 | ADMIN이 관리자 API 접근 | @WithMockUser(roles="ADMIN") → GET /api/admin/members | 200 |

### 8.4 입력값 보안 테스트

| TC ID | 테스트 케이스 | 방법 | 기대 결과 |
|-------|-------------|------|----------|
| SEC-10 | SQL 인젝션 시도 | keyword="'; DROP TABLE owner; --" | 정상 검색 (SQL 실행 안 됨), 결과 0건 |

---

## 9. 테스트 데이터 설계

### 9.1 테스트 픽스처

각 테스트 클래스에서 공통으로 사용하는 테스트 데이터 빌더입니다.

```java
// TestFixtures.java — 테스트용 데이터 생성 유틸리티

public class TestFixtures {

    public static Member createAdmin() {
        return Member.builder()
            .email("admin@payline.com")
            .password("$2a$10$...")  // BCrypt("Admin123!")
            .name("김정산")
            .role(MemberRole.ADMIN)
            .build();
    }

    public static Member createUser() {
        return Member.builder()
            .email("user@payline.com")
            .password("$2a$10$...")
            .name("이서비스")
            .role(MemberRole.USER)
            .build();
    }

    public static Owner createOwner(String businessName, String businessNumber) {
        return Owner.builder()
            .businessName(businessName)
            .businessNumber(businessNumber)
            .ownerName("테스트대표")
            .phone("010-1234-5678")
            .build();
    }

    public static Order createCompletedOrder(Owner owner, BigDecimal totalAmount) {
        return Order.builder()
            .owner(owner)
            .orderDateTime(LocalDateTime.now().minusDays(1))
            .status(OrderStatus.COMPLETED)
            .totalAmount(totalAmount)
            .build();
    }

    public static OrderDetail createOrderDetail(
            Order order, PaymentMethod method,
            BigDecimal paymentAmount, BigDecimal feeAmount) {
        return OrderDetail.builder()
            .order(order)
            .paymentMethod(method)
            .paymentAmount(paymentAmount)
            .feeAmount(feeAmount)
            .build();
    }

    public static Reward createReward(Owner owner, BigDecimal amount) {
        return Reward.builder()
            .owner(owner)
            .rewardAmount(amount)
            .reason(RewardReason.DELIVERY_ACCIDENT)
            .reasonDetail("테스트 보상 사유입니다. 10자 이상 작성합니다.")
            .rewardDateTime(LocalDateTime.now().minusDays(1))
            .settled(false)
            .build();
    }

    // Enum 전체 커버리지용 팩토리 메서드
    public static Reward createRewardWithReason(Owner owner, RewardReason reason) {
        // DELIVERY_ACCIDENT, SYSTEM_ERROR, PROMOTION, ETC 모두 사용
        return Reward.builder()
            .owner(owner)
            .rewardAmount(new BigDecimal("10000"))
            .reason(reason)
            .reasonDetail("테스트 보상 사유입니다. 10자 이상 작성합니다.")
            .rewardDateTime(LocalDateTime.now().minusDays(1))
            .settled(false)
            .build();
    }

    public static OrderDetail createDetailWithMethod(Order order, PaymentMethod method) {
        // CARD, CASH, BAEMIN_PAY, BANK_TRANSFER 모두 사용
        return OrderDetail.builder()
            .order(order)
            .paymentMethod(method)
            .paymentAmount(new BigDecimal("10000"))
            .feeAmount(new BigDecimal("300"))
            .build();
    }

    public static SettleCreateRequest createSettleRequest(Long ownerId) {
        return new SettleCreateRequest(
            ownerId,
            LocalDate.now().minusDays(7),
            LocalDate.now().minusDays(1)
        );
    }
}
```

### 9.2 테스트 시나리오별 데이터 세트

#### 지급 금액 산출 검증 데이터

```
Owner: 행복치킨 (ID=1)

주문 3건 (COMPLETED, 기간 내):
  Order#1: totalAmount = 100,000
    - Detail#1: CARD,      paymentAmount = 70,000,  feeAmount = 2,100
    - Detail#2: BAEMIN_PAY, paymentAmount = 30,000,  feeAmount = 900
  Order#2: totalAmount = 50,000
    - Detail#3: CASH,      paymentAmount = 50,000,  feeAmount = 0
  Order#3: totalAmount = 80,000
    - Detail#4: CARD,      paymentAmount = 80,000,  feeAmount = 2,400

주문 1건 (CANCELLED, 기간 내 — 제외 대상):
  Order#4: totalAmount = 30,000
    - Detail#5: CARD,      paymentAmount = 30,000,  feeAmount = 900

보상금 2건 (미정산):
  Reward#1: rewardAmount = 15,000
  Reward#2: rewardAmount = 5,000

[기대 산출 결과]
totalOrderAmount  = 70,000 + 30,000 + 50,000 + 80,000 = 230,000
totalFeeAmount    = 2,100 + 900 + 0 + 2,400 = 5,400
totalRewardAmount = 15,000 + 5,000 = 20,000
settlementAmount  = (230,000 - 5,400) + 20,000 = 244,600
```

#### BigDecimal 정밀도 검증 데이터

```
Order#5: totalAmount = 33,333.33
  - Detail#6: CARD, paymentAmount = 33,333.33, feeAmount = 999.99

[검증]
- feeAmount: 999.99 (부동소수점 0.99999... 아님)
- settlementAmount 계산 시 BigDecimal.subtract 사용 확인
- double/float 사용 시 실패하는 케이스
```

---

## 10. 테스트 실행 가이드

### 10.1 실행 명령어

```bash
# 전체 테스트 실행
./gradlew test

# 특정 도메인 테스트만 실행
./gradlew test --tests "com.payline.settle.*"

# 단위 테스트만 실행 (태그 기반)
./gradlew test -Dgroups="unit"

# 통합 테스트만 실행
./gradlew test -Dgroups="integration"

# 커버리지 리포트 생성
./gradlew test jacocoTestReport
# 리포트 위치: build/reports/jacoco/test/html/index.html
```

### 10.2 테스트 디렉토리 구조

```
src/test/java/com/payline/
+-- global/
|   +-- auth/
|       +-- CustomUserDetailsServiceTest.java
+-- member/
|   +-- service/
|   |   +-- MemberServiceTest.java
|   +-- controller/
|       +-- MemberControllerIntegrationTest.java
+-- owner/
|   +-- service/
|   |   +-- OwnerServiceTest.java
|   +-- controller/
|       +-- OwnerControllerIntegrationTest.java
+-- order/
|   +-- service/
|   |   +-- OrderServiceTest.java
|   +-- domain/
|   |   +-- OrderTest.java
|   +-- controller/
|       +-- OrderControllerIntegrationTest.java
+-- reward/
|   +-- service/
|   |   +-- RewardServiceTest.java
|   +-- controller/
|       +-- RewardControllerIntegrationTest.java
+-- settle/
|   +-- domain/
|   |   +-- SettleStatusTest.java
|   +-- service/
|   |   +-- SettlementCalculatorTest.java
|   |   +-- SettleServiceTest.java
|   +-- controller/
|       +-- SettleControllerIntegrationTest.java
+-- security/
|   +-- AuthSecurityTest.java
|   +-- CsrfSecurityTest.java
|   +-- RoleSecurityTest.java
|   +-- InputSecurityTest.java
+-- support/
    +-- TestFixtures.java
```

### 10.3 커버리지 기준

| 대상 | 목표 | 측정 |
|------|------|------|
| Service 계층 전체 | 80% 이상 | Line Coverage |
| SettleService | 90% 이상 (핵심 도메인) | Line Coverage |
| SettlementCalculator | 95% 이상 (금액 산출) | Line + Branch |
| SettleStatus | 100% (상태 전이) | Branch Coverage |
| Controller 계층 | 주요 엔드포인트 100% | 통합 테스트로 커버 |

### 10.4 테스트 케이스 요약 통계

| 도메인 | 단위 | 통합 | 보안 | 합계 |
|--------|:---:|:---:|:---:|:---:|
| Auth | 3 | 6 | — | 9 |
| Member | 8 | 4 | — | 12 |
| Owner | 7 | 4 | — | 11 |
| Order | 11 | 3 | — | 14 |
| Reward | 6 | 2 | — | 8 |
| Settle | 29 | 9 | — | 38 |
| Security (횡단) | — | — | 10 | 10 |
| **합계** | **52** | **25** | **10** | **87** |

> Settle 도메인이 전체 테스트의 44%를 차지합니다. 이는 정산 시스템의 핵심 도메인으로서 금액 산출 정확성, 상태 전이 규칙, 데이터 무결성 등 검증할 비즈니스 규칙이 가장 많기 때문입니다.

---

> **문서 이력**
>
> | 버전 | 날짜 | 변경 내용 |
> |------|------|-----------|
> | v1.0 | 2026-02-12 | 최초 작성. 87개 테스트 케이스 (단위 52 + 통합 25 + 보안 10) |
