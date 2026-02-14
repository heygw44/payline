# 🏗️ 아키텍처 설계서: 페이라인 (PayLine)

### 우아한형제들 정산시스템 클론 코딩 — 시스템 아키텍처

> _PRD v3.0.1 기반 기술 아키텍처 상세 설계_

---

| 항목 | 내용 |
|------|------|
| **프로젝트명** | 페이라인 (PayLine) |
| **문서 유형** | 시스템 아키텍처 설계서 |
| **문서 버전** | v1.3 |
| **최종 수정일** | 2026년 2월 14일 |
| **기반 문서** | PRD v3.0.1 |
| **기술 스택** | Java 21 + Spring Boot 4.0.2 + Vue 3 + MySQL 9.x |

---

## 목차

1. [시스템 전체 아키텍처](#1-시스템-전체-아키텍처)
   - 1.1 시스템 컨텍스트 다이어그램
   - 1.2 컨테이너 다이어그램
   - 1.3 배포 아키텍처
   - 1.4 기술 스택 버전 요약
2. [백엔드 아키텍처 상세](#2-백엔드-아키텍처-상세)
   - 2.1 레이어드 아키텍처 설계
   - 2.2 패키지 구조 상세
   - 2.3 계층 간 데이터 흐름
   - 2.4 의존성 규칙
3. [도메인 모델 설계](#3-도메인-모델-설계)
   - 3.1 ERD (Entity Relationship Diagram)
   - 3.2 엔티티 상세 설계
   - 3.3 엔티티 연관관계 매핑
   - 3.4 Enum 정의
   - 3.5 BaseEntity 설계
4. [인증/인가 아키텍처](#4-인증인가-아키텍처)
   - 4.1 Spring Security 필터 체인
   - 4.2 세션 관리 아키텍처
   - 4.3 CSRF 토큰 처리 플로우
   - 4.4 SecurityConfig 설계
5. [API 설계 상세](#5-api-설계-상세)
   - 5.1 RESTful API 설계 원칙
   - 5.2 전체 API 명세
   - 5.3 요청/응답 DTO 설계
   - 5.4 공통 응답 래퍼
   - 5.5 에러 코드 체계
6. [핵심 비즈니스 로직 설계](#6-핵심-비즈니스-로직-설계)
   - 6.1 지급 금액 산출 알고리즘
   - 6.2 지급 상태 머신 구현
   - 6.3 데이터 정합성 검증
   - 6.4 트랜잭션 경계 설계
7. [데이터 액세스 계층 설계](#7-데이터-액세스-계층-설계)
   - 7.1 JPA + MyBatis 병행 구조
   - 7.2 JPA Repository 설계
   - 7.3 MyBatis Mapper 설계
   - 7.4 쿼리 최적화 전략
8. [프론트엔드 아키텍처](#8-프론트엔드-아키텍처)
   - 8.1 Vue 3 애플리케이션 구조
   - 8.2 상태 관리 설계 (Pinia)
   - 8.3 라우팅 및 네비게이션 가드
   - 8.4 API 통신 계층
   - 8.5 컴포넌트 설계
9. [횡단 관심사](#9-횡단-관심사)
   - 9.1 예외 처리 아키텍처
   - 9.2 로깅 전략
   - 9.3 유효성 검증 계층
   - 9.4 Auditing 설계
   - 9.5 보안 방어 아키텍처
10. [데이터베이스 설계](#10-데이터베이스-설계)
    - 10.1 테이블 정의서
    - 10.2 인덱스 전략
    - 10.3 데이터 마이그레이션
    - 10.4 시드 데이터

---

## 1. 시스템 전체 아키텍처

### 1.1 시스템 컨텍스트 다이어그램

시스템 외부에서 바라본 페이라인의 전체 모습입니다.

```
                          +------------------+
                          |   정산팀 직원     |
                          |   (관리자/ADMIN)  |
                          +--------+---------+
                                   |
                                   | HTTPS
                                   v
+------------------+      +------------------+      +------------------+
|   CS팀 직원       | ---> |                  | <--- |   가맹점 업주     |
|   (일반회원/USER) |      |   페이라인        |      |   (간접 사용자)   |
+------------------+      |   (PayLine)      |      +------------------+
                          |                  |
                          |   정산 어드민     |
                          |   시스템          |
                          +------------------+
                                   |
                                   | JDBC
                                   v
                          +------------------+
                          |   MySQL 9.x      |
                          |   Database       |
                          +------------------+
```

### 1.2 컨테이너 다이어그램

시스템 내부의 주요 컨테이너(배포 단위) 구성입니다.

```
+------------------------------------------------------------------+
|                        페이라인 (PayLine)                          |
|                                                                    |
|  +---------------------------+    +----------------------------+  |
|  |   Frontend Container      |    |   Backend Container        |  |
|  |                           |    |                            |  |
|  |  +---------------------+  |    |  +----------------------+  |  |
|  |  |  Vue 3 SPA          |  |    |  |  Spring Boot 4.0.2   |  |  |
|  |  |  - Composition API  |  |    |  |  - Java 21           |  |  |
|  |  |  - Pinia Store      | -------> |  - Spring Security 7.0.2| |  |
|  |  |  - Vue Router       |  | API|  |  - Spring MVC        |  |  |
|  |  |  - Axios Client     |  |    |  |  - JPA (Hibernate 7.2.1)| |  |
|  |  |                     |  |    |  |  - MyBatis 3.5.x     |  |  |
|  |  +---------------------+  |    |  +----------+-----------+  |  |
|  |                           |    |             |              |  |
|  |  Vite Dev Server (:5173)  |    |  Embedded Tomcat (:8080)  |  |
|  +---------------------------+    +----------+--+--------------+  |
|                                              |  |                 |
+----------------------------------------------+--+-----------------+
                                               |  |
                                   +-----------+  +-----------+
                                   |                          |
                                   v                          v
                          +------------------+    +------------------+
                          |  MySQL 9.x       |    |  H2 Database     |
                          |  (운영 환경)      |    |  (개발/테스트)    |
                          |  Port: 3306      |    |  In-Memory       |
                          +------------------+    +------------------+
```

### 1.3 배포 아키텍처

#### 개발 환경

```
[개발자 PC]
  |
  +-- [Vite Dev Server :5173] --proxy--> [Spring Boot :8080] --> [H2 In-Memory]
  |         (Vue 3 HMR)                   (API Server)
  |
  +-- [H2 Console :8080/h2-console]  (DB 확인용)
```

#### 운영 환경 (선택적 확장)

```
[Browser] --> [Nginx :80/443] --+-- /           --> [Vue 빌드 정적 파일]
                                |
                                +-- /api/**     --> [Spring Boot :8080] --> [MySQL :3306]
```

### 1.4 기술 스택 버전 요약

본 설계서에서 사용하는 기술 스택의 정확한 버전입니다 (PRD v3.0.1 기준).

| 구분 | 기술 | 버전 |
|------|------|------|
| **언어** | Java | 21 (LTS) |
| **프레임워크** | Spring Boot | 4.0.2 |
| **코어** | Spring Framework | 7.0.3 |
| **보안** | Spring Security | 7.0.2 |
| **ORM** | Hibernate | 7.2.1 |
| **SQL 매퍼** | MyBatis | 3.5.x (mybatis-spring-boot-starter 4.x) |
| **프론트엔드** | Vue 3 (Composition API) | 3.x |
| **프론트 빌드** | Vite | 6.x |
| **상태 관리** | Pinia | 3.x |
| **HTTP 클라이언트** | Axios | 1.x |
| **빌드 도구** | Gradle | Groovy DSL (9.x / 최소 8.14) |
| **유틸리티** | Lombok | 최신 |
| **테스트** | JUnit Jupiter | 6.0.2 (Spring Boot 관리) |
| **테스트 모킹** | Mockito | 5.20 (Spring Boot 관리) |
| **API 문서** | SpringDoc OpenAPI | 3.x (Boot 4 호환) |
| **DB (운영)** | MySQL | 9.x |
| **DB (개발)** | H2 | In-Memory |

---

## 2. 백엔드 아키텍처 상세

### 2.1 레이어드 아키텍처 설계

페이라인는 **계층형 아키텍처(Layered Architecture)**를 채택합니다. 각 계층은 단방향 의존성을 가지며, 상위 계층은 하위 계층에만 의존합니다.

```
+==============================================================+
|                     Presentation Layer                         |
|  +--------------------------------------------------------+  |
|  |  @RestController                                        |  |
|  |  - HTTP 요청/응답 처리                                    |  |
|  |  - 입력값 유효성 검증 (@Valid)                             |  |
|  |  - DTO 변환 위임                                         |  |
|  |  - 응답 래핑 (ApiResponse)                               |  |
|  +--------------------------------------------------------+  |
|                           | DTO                                |
|                           v                                    |
+==============================================================+
|                      Business Layer                            |
|  +--------------------------------------------------------+  |
|  |  @Service + @Transactional                              |  |
|  |  - 핵심 비즈니스 로직                                     |  |
|  |  - 트랜잭션 경계 관리                                     |  |
|  |  - 도메인 규칙 검증                                       |  |
|  |  - 도메인 객체 간 협력 조율                                |  |
|  +--------------------------------------------------------+  |
|                           | Entity / DTO                       |
|                           v                                    |
+==============================================================+
|                     Persistence Layer                          |
|  +------------------------+  +-----------------------------+  |
|  |  JPA Repository        |  |  MyBatis Mapper             |  |
|  |  - 엔티티 CRUD         |  |  - 복잡한 검색 쿼리          |  |
|  |  - 연관관계 관리        |  |  - 집계 쿼리 (SUM, GROUP BY)|  |
|  |  - Spring Data JPA     |  |  - 동적 조건 검색           |  |
|  +------------------------+  +-----------------------------+  |
|                           | SQL                                |
|                           v                                    |
+==============================================================+
|                      Database Layer                            |
|  +--------------------------------------------------------+  |
|  |  MySQL 9.x / H2                                        |  |
|  +--------------------------------------------------------+  |
+==============================================================+
```

#### 계층별 책임과 제약

| 계층 | 책임 | 허용 | 금지 |
|------|------|------|------|
| **Presentation** | HTTP 요청/응답 처리, 입력 검증, 응답 포맷팅 | Controller, DTO, Validator | Entity 직접 노출, 비즈니스 로직 |
| **Business** | 비즈니스 로직, 트랜잭션 관리, 도메인 규칙 검증 | Service, Domain Entity, DTO 변환 | HTTP 관련 코드, SQL 직접 작성 |
| **Persistence** | 데이터 저장/조회, 쿼리 실행 | Repository, Mapper, Entity | 비즈니스 로직, HTTP 관련 코드 |
| **Database** | 데이터 물리 저장, 제약 조건, 인덱스 | DDL, DML | — |

### 2.2 패키지 구조 상세

**도메인별 패키지 분리** 전략을 채택합니다. 각 도메인은 독립적인 패키지 안에 모든 계층의 클래스를 포함합니다.

```
src/main/java/com/payline/
|
+-- PaylineApplication.java                  # Spring Boot 메인
|
+-- global/                                  # ── 전역 공통 ──
|   +-- config/
|   |   +-- SecurityConfig.java              # Spring Security 설정 (향후 구현)
|   |   +-- WebMvcConfig.java                # CORS, 인터셉터 설정 (향후 구현)
|   |   +-- MyBatisConfig.java               # MyBatis 설정 (향후 구현)
|   |   +-- JpaAuditingConfig.java           # JPA Auditing 활성화 ✅
|   |   +-- SwaggerConfig.java               # SpringDoc OpenAPI 설정 (향후 구현)
|   |
|   +-- common/
|   |   +-- dto/
|   |   |   +-- ApiResponse.java             # 공통 API 응답 래퍼
|   |   |   +-- PageResponse.java            # 페이징 응답 DTO
|   |   +-- (기타 공통 유틸)
|   |
|   +-- entity/
|   |   +-- BaseEntity.java                  # 공통 Auditing 엔티티
|   |   +-- BaseTimeEntity.java              # 시간 필드만 포함
|   |
|   +-- error/
|   |   +-- GlobalExceptionHandler.java      # @RestControllerAdvice
|   |   +-- ErrorCode.java                   # 에러 코드 Enum
|   |   +-- BusinessException.java           # 비즈니스 예외 (concrete, ErrorCode 래핑)
|   |
|   +-- auth/
|       +-- CustomUserDetailsService.java    # UserDetailsService 구현
|       +-- CustomUserDetails.java           # UserDetails 구현
|       +-- LoginSuccessHandler.java         # 인증 성공 핸들러 (JSON)
|       +-- LoginFailureHandler.java         # 인증 실패 핸들러 (JSON)
|       +-- CustomAccessDeniedHandler.java   # 403 핸들러
|       +-- CustomAuthenticationEntryPoint.java  # 401 핸들러
|       +-- (AuditorAware는 JpaAuditingConfig Bean으로 제공)
|
+-- member/                                  # ── 회원 도메인 ──
|   +-- controller/
|   |   +-- MemberController.java            # 일반 회원 API (/api/members)
|   |   +-- AdminMemberController.java       # 관리자 전용 (/api/admin/members)
|   +-- service/
|   |   +-- MemberService.java               # 비즈니스 로직
|   +-- repository/
|   |   +-- MemberRepository.java            # JPA Repository
|   +-- domain/
|   |   +-- Member.java                      # Entity
|   |   +-- MemberRole.java                  # Enum (ADMIN, USER)
|   +-- dto/
|       +-- SignupRequest.java               # record
|       +-- MemberResponse.java              # record
|       +-- MemberUpdateRequest.java         # record
|       +-- RoleChangeRequest.java           # record
|
+-- owner/                                   # ── 업주 도메인 ──
|   +-- controller/
|   |   +-- OwnerController.java
|   +-- service/
|   |   +-- OwnerService.java
|   +-- repository/
|   |   +-- OwnerRepository.java             # JPA
|   |   +-- OwnerSearchMapper.java           # MyBatis 검색
|   +-- domain/
|   |   +-- Owner.java
|   +-- dto/
|       +-- OwnerCreateRequest.java          # record
|       +-- OwnerUpdateRequest.java          # record
|       +-- OwnerResponse.java               # record
|       +-- OwnerDetailResponse.java         # record (요약 통계 포함)
|       +-- OwnerSearchCondition.java        # record (검색 조건)
|
+-- order/                                   # ── 주문 도메인 ──
|   +-- controller/
|   |   +-- OrderController.java
|   |   +-- OrderDetailController.java
|   +-- service/
|   |   +-- OrderService.java
|   |   +-- OrderDetailService.java
|   +-- repository/
|   |   +-- OrderRepository.java             # JPA
|   |   +-- OrderDetailRepository.java       # JPA
|   |   +-- OrderSearchMapper.java           # MyBatis 검색
|   +-- domain/
|   |   +-- Order.java
|   |   +-- OrderDetail.java
|   |   +-- OrderStatus.java                 # Enum
|   |   +-- PaymentMethod.java               # Enum
|   +-- dto/
|       +-- OrderCreateRequest.java          # record
|       +-- OrderDetailCreateRequest.java    # record
|       +-- OrderResponse.java               # record
|       +-- OrderSearchCondition.java        # record
|
+-- reward/                                  # ── 보상금 도메인 ──
|   +-- controller/
|   |   +-- RewardController.java
|   +-- service/
|   |   +-- RewardService.java
|   +-- repository/
|   |   +-- RewardRepository.java            # JPA
|   |   +-- RewardSearchMapper.java          # MyBatis 검색
|   +-- domain/
|   |   +-- Reward.java
|   |   +-- RewardReason.java                # Enum
|   +-- dto/
|       +-- RewardCreateRequest.java         # record
|       +-- RewardResponse.java              # record
|       +-- RewardSearchCondition.java       # record
|
+-- settle/                                  # ── 지급 도메인 ──
    +-- controller/
    |   +-- SettleController.java
    +-- service/
    |   +-- SettleService.java
    |   +-- SettlementCalculator.java        # 금액 산출 전담 클래스
    +-- repository/
    |   +-- SettleRepository.java            # JPA
    |   +-- SettleSearchMapper.java          # MyBatis 검색
    |   +-- SettlementMapper.java            # MyBatis 집계 쿼리
    +-- domain/
    |   +-- Settle.java
    |   +-- SettleStatus.java                # Enum + 상태 전이 로직
    +-- dto/
        +-- SettleCreateRequest.java         # record
        +-- SettleResponse.java              # record
        +-- SettleDetailResponse.java        # record (주문/보상 포함)
        +-- SettleSearchCondition.java       # record
        +-- SettlementSummary.java           # record (금액 산출 결과)
```

#### MyBatis XML 매퍼 위치

```
src/main/resources/
+-- mapper/
|   +-- OwnerSearchMapper.xml
|   +-- OrderSearchMapper.xml
|   +-- RewardSearchMapper.xml
|   +-- SettleSearchMapper.xml
|   +-- SettlementMapper.xml                 # 집계 쿼리 전용
+-- application.yml
+-- application-dev.yml
+-- application-prod.yml
```

### 2.3 계층 간 데이터 흐름

일반적인 API 요청의 전체 데이터 흐름을 추적합니다.

```
[Vue SPA]
    | HTTP POST /api/settles { ownerId, startDate, endDate }
    | Cookie: JSESSIONID=xxx
    | Header: X-XSRF-TOKEN=yyy
    v
[Spring Security Filter Chain]
    | 1. CsrfFilter: CSRF 토큰 검증
    | 2. SessionManagementFilter: 세션 유효성 확인
    | 3. FilterSecurityInterceptor: URL 패턴 권한 확인 (ADMIN)
    v
[SettleController]
    | @Valid SettleCreateRequest (Bean Validation)
    | record → 서비스 호출
    v
[SettleService]
    | @Transactional
    | 1. Owner 존재 확인 (OwnerRepository.findById)
    | 2. 중복 정산 검증 (SettleRepository.existsByOwnerAndPeriod)
    | 3. 금액 산출 위임 (SettlementCalculator)
    |     +-- SettlementMapper.calculateOrderAmount  (MyBatis)
    |     +-- SettlementMapper.calculateRewardAmount (MyBatis)
    | 4. Settle 엔티티 생성 + 저장 (SettleRepository.save, JPA)
    | 5. Entity → SettleResponse (record) 변환
    v
[SettleController]
    | ApiResponse.success(settleResponse)
    v
[Vue SPA]
    | HTTP 201 Created
    | { "success": true, "data": { ... } }
```

### 2.4 의존성 규칙

```
Controller  -->  Service  -->  Repository
    |               |               |
    v               v               v
  DTO           Entity/DTO       Entity

[금지 사항]
- Controller --> Repository (Service를 반드시 경유)
- Service --> Controller (역방향 의존 금지)
- Entity를 Controller 응답으로 직접 반환 금지
- Repository에 비즈니스 로직 배치 금지
```

---

## 3. 도메인 모델 설계

### 3.1 ERD (Entity Relationship Diagram)

```
+-------------------+
|      member       |
+-------------------+
| PK  id            |
|     email         |
|     password      |
|     name          |
|     role          |
|     deleted       |
|     created_at    |
|     updated_at    |
|     created_by    |
|     updated_by    |
+-------------------+


+-------------------+        +-------------------+        +-------------------+
|      owner        |   1:N  |      order        |   1:N  |   order_detail    |
+-------------------+<-------+-------------------+<-------+-------------------+
| PK  id            |        | PK  id            |        | PK  id            |
|     business_name |        | FK  owner_id      |        | FK  order_id      |
|     business_no   |        |     order_date    |        |     payment_method|
|     owner_name    |        |     status        |        |     payment_amount|
|     phone         |        |     total_amount  |        |     fee_amount    |
|     email         |        |     created_at    |        |     created_at    |
|     address       |        |     updated_at    |        |     updated_at    |
|     bank_name     |        |     created_by    |        |     created_by    |
|     account_no    |        |     updated_by    |        |     updated_by    |
|     deleted       |        +-------------------+        +-------------------+
|     created_at    |
|     updated_at    |        +-------------------+
|     created_by    |   1:N  |      reward       |
|     updated_by    |<-------+-------------------+
+-------------------+        | PK  id            |
        ^                    | FK  owner_id      |
        |                    |     reward_amount  |
        |  1:N               |     reason        |
        |                    |     reason_detail  |
+-------------------+        |     reward_date   |
|      settle       |        |     settled       |
+-------------------+        |     created_at    |
| PK  id            |        |     updated_at    |
| FK  owner_id      |        |     created_by    |
|     start_date    |        |     updated_by    |
|     end_date      |        +-------------------+
|     total_order   |
|     total_fee     |
|     total_reward  |
|     settle_amount |
|     status        |
|     reject_reason |
|     created_at    |
|     updated_at    |
|     created_by    |
|     updated_by    |
+-------------------+
```

### 3.2 엔티티 상세 설계

#### Member Entity

```java
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;               // BCrypt 해시

    @Column(nullable = false, length = 20)
    private String name;

    @Enumerated(STRING)
    @Column(nullable = false, length = 10)
    private MemberRole role;               // ADMIN, USER

    @Column(nullable = false)
    private boolean deleted = false;       // soft delete

    // 비즈니스 메서드
    public void changeRole(MemberRole newRole) { ... }
    public void updateInfo(String name, String encodedPassword) { ... }
    public void softDelete() { this.deleted = true; }
}
```

#### Owner Entity

```java
@Entity
@Table(name = "owner")
public class Owner extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String businessName;

    @Column(nullable = false, unique = true, length = 12)
    private String businessNumber;         // 사업자번호

    @Column(nullable = false, length = 20)
    private String ownerName;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 200)
    private String address;

    @Column(length = 50)
    private String bankName;

    @Column(length = 50)
    private String accountNumber;

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "owner", fetch = LAZY)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = LAZY)
    private List<Reward> rewards = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = LAZY)
    private List<Settle> settles = new ArrayList<>();
}
```

#### Order / OrderDetail Entity

```java
@Entity
@Table(name = "orders")               // order는 SQL 예약어이므로 orders
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Enumerated(STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;            // RECEIVED, COMPLETED, CANCELLED

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // 비즈니스 메서드
    public void addOrderDetail(OrderDetail detail) { ... }
    public void validateTotalAmount() { ... }  // detail 합계 검증
    public void cancel() { this.status = OrderStatus.CANCELLED; }
    public boolean isCompleted() { return this.status == OrderStatus.COMPLETED; }
}

@Entity
@Table(name = "order_detail")
public class OrderDetail extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod paymentMethod;   // CARD, CASH, BAEMIN_PAY, BANK_TRANSFER

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal paymentAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal feeAmount;
}
```

#### Reward Entity

```java
@Entity
@Table(name = "reward")
public class Reward extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal rewardAmount;

    @Enumerated(STRING)
    @Column(nullable = false, length = 30)
    private RewardReason reason;

    @Column(nullable = false, length = 500)
    private String reasonDetail;

    @Column(nullable = false)
    private LocalDateTime rewardDateTime;

    @Column(nullable = false)
    private boolean settled = false;       // 정산 포함 여부

    // 비즈니스 메서드
    public void markAsSettled() { this.settled = true; }
    public boolean isModifiable() { return !this.settled; }
}
```

#### Settle Entity

```java
@Entity
@Table(name = "settle")
public class Settle extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(nullable = false)
    private LocalDate settleStartDate;

    @Column(nullable = false)
    private LocalDate settleEndDate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalOrderAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalFeeAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalRewardAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal settlementAmount;   // 최종 지급 금액

    @Enumerated(STRING)
    @Column(nullable = false, length = 20)
    private SettleStatus status;

    @Column(length = 500)
    private String rejectionReason;

    // 상태 전이 비즈니스 메서드
    public void request()  { this.status = status.transitionTo(REQUESTED); }
    public void approve()  { this.status = status.transitionTo(APPROVED); }
    public void complete() { this.status = status.transitionTo(COMPLETED); }
    public void reject(String reason) {
        this.status = status.transitionTo(REJECTED);
        this.rejectionReason = reason;
    }
    public boolean isDeletable() { return this.status == PENDING; }
}
```

### 3.3 엔티티 연관관계 매핑

| 관계 | 방향 | 매핑 | Fetch 전략 | Cascade |
|------|------|------|-----------|---------|
| Owner ↔ Order | 양방향 | `@OneToMany` / `@ManyToOne` | LAZY | 없음 |
| Owner ↔ Reward | 양방향 | `@OneToMany` / `@ManyToOne` | LAZY | 없음 |
| Owner ↔ Settle | 양방향 | `@OneToMany` / `@ManyToOne` | LAZY | 없음 |
| Order ↔ OrderDetail | 양방향 | `@OneToMany` / `@ManyToOne` | LAZY | ALL + orphanRemoval |

> **설계 원칙:** 모든 `@ManyToOne`은 `LAZY` 로딩. `@OneToMany`는 필요한 경우에만 양방향 설정하며, Cascade는 OrderDetail처럼 라이프사이클이 부모에 종속되는 경우에만 적용합니다.

### 3.4 Enum 정의

```java
public enum MemberRole    { ADMIN, USER }
public enum OrderStatus   { RECEIVED, COMPLETED, CANCELLED }
public enum PaymentMethod { CARD, CASH, BAEMIN_PAY, BANK_TRANSFER }
public enum RewardReason  { DELIVERY_ACCIDENT, SYSTEM_ERROR, PROMOTION, ETC }

public enum SettleStatus {
    PENDING, REQUESTED, APPROVED, COMPLETED, REJECTED;

    public boolean canTransitionTo(SettleStatus target) {
        return switch (this) {
            case PENDING -> target == REQUESTED;
            case REQUESTED -> target == APPROVED || target == REJECTED;
            case APPROVED -> target == COMPLETED;
            case COMPLETED, REJECTED -> false;
        };
    }
}
```

### 3.5 BaseEntity 설계

2계층 구조: `BaseTimeEntity`(시간 필드) → `BaseEntity`(작성자/수정자 추가)

```java
// 시간 필드만 포함 (createdAt, updatedAt)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

// 작성자/수정자 포함 (BaseTimeEntity 확장)
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(updatable = false, length = 100, nullable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(length = 100)
    private String updatedBy;
}
```

> **Lombok 활용 원칙:** 모든 Entity에 `@Getter` 적용. `@Setter`는 사용하지 않고 비즈니스 메서드로 상태 변경. DTO(record)에는 Lombok 불필요. Service/Controller에 `@RequiredArgsConstructor` 적용. 빌드 도구는 **Gradle (Groovy DSL)**을 사용하며, Lombok은 `annotationProcessor`로 등록합니다.

---

## 4. 인증/인가 아키텍처

### 4.1 Spring Security 필터 체인

```
[HTTP Request]
    |
    v
SecurityFilterChain (순서)
    |
    +-- 1. CorsFilter                      CORS 프리플라이트 처리
    +-- 2. CsrfFilter                      CSRF 토큰 검증 (POST/PUT/PATCH/DELETE)
    +-- 3. UsernamePasswordAuthFilter       POST /api/login 처리
    +-- 4. SecurityContextPersistenceFilter 세션에서 SecurityContext 복원
    +-- 5. SessionManagementFilter          동시 세션 제어, 세션 고정 방어
    +-- 6. ExceptionTranslationFilter       401/403 예외 핸들링
    +-- 7. FilterSecurityInterceptor        URL 패턴 기반 인가
    |
    v
[Controller]
```

### 4.2 세션 관리 아키텍처

```
[로그인 성공]
    |
    v
1. SecurityContext에 Authentication 저장
2. HttpSession 생성 (JSESSIONID)
3. SessionRegistry에 세션 등록
4. LoginSuccessHandler가 JSON 응답 반환
    |
    v
[이후 요청마다]
    |
    v
1. JSESSIONID 쿠키에서 세션 조회
2. 세션에서 SecurityContext 복원
3. Authentication 객체로 인가 판단
    |
    v
[로그아웃]
    |
    v
1. HttpSession.invalidate()
2. SecurityContext 클리어
3. JSESSIONID 쿠키 삭제
4. SessionRegistry에서 제거
```

### 4.3 CSRF 토큰 처리 플로우

```
[Spring Security]
    | CookieCsrfTokenRepository 설정
    | withHttpOnlyFalse() -- JavaScript에서 읽을 수 있도록
    v
[응답 Set-Cookie: XSRF-TOKEN=abc123]
    |
    v
[Axios 인터셉터]
    | document.cookie에서 XSRF-TOKEN 읽기
    | 요청 헤더에 X-XSRF-TOKEN: abc123 자동 첨부
    |   (Axios의 xsrfCookieName / xsrfHeaderName 설정으로 자동화)
    v
[Spring Security CsrfFilter]
    | 쿠키의 토큰 vs 헤더의 토큰 비교 검증
    | 일치 --> 요청 허용
    | 불일치 --> 403 Forbidden
```

### 4.4 SecurityConfig 설계

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity                        // @PreAuthorize 활성화
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
            // CSRF: CookieCsrfTokenRepository 사용 (Vue Axios 연동)
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))

            // 인가 규칙
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(POST, "/api/members/signup").permitAll()
                .requestMatchers(POST, "/api/login").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll())            // 정적 리소스

            // 폼 로그인 (JSON 응답)
            .formLogin(form -> form
                .loginProcessingUrl("/api/login")
                .usernameParameter("email")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler))

            // 로그아웃
            .logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(/* JSON 응답 */)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"))

            // 세션 관리
            .sessionManagement(session -> session
                .sessionCreationPolicy(IF_REQUIRED)
                .sessionFixation().changeSessionId()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false))

            // 예외 핸들링
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authEntryPoint)     // 401
                .accessDeniedHandler(accessDeniedHandler))    // 403

            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
```

---

## 5. API 설계 상세

### 5.1 RESTful API 설계 원칙

| 원칙 | 적용 |
|------|------|
| **리소스 중심 URL** | 명사형 복수 (`/api/owners`, `/api/settles`) |
| **HTTP 메서드 의미** | GET(조회), POST(생성), PUT(전체 수정), PATCH(부분 수정), DELETE(삭제) |
| **상태 코드** | 200(성공), 201(생성), 204(삭제 성공), 400(검증 실패), 401(미인증), 403(권한 없음), 404(미존재), 409(충돌/중복) |
| **일관된 응답 포맷** | `ApiResponse<T>` 래퍼로 모든 응답 통일 |
| **하위 리소스** | `/api/owners/{id}/orders`, `/api/orders/{id}/details` |
| **상태 전이 동사** | PATCH 메서드 + 동사 URL (`/api/settles/{id}/approve`) |

### 5.2 전체 API 명세

#### 인증 API

| Method | URL | 설명 | 요청 Body | 응답 | 상태코드 |
|--------|-----|------|----------|------|---------|
| POST | `/api/members/signup` | 회원가입 | `SignupRequest` | `MemberResponse` | 201 |
| POST | `/api/login` | 로그인 | `email`, `password` (form) | `MemberResponse` | 200 |
| POST | `/api/logout` | 로그아웃 | — | — | 200 |
| GET | `/api/members/me` | 내 정보 | — | `MemberResponse` | 200 |
| PUT | `/api/members/me` | 내 정보 수정 | `MemberUpdateRequest` | `MemberResponse` | 200 |

#### 관리자 회원 API

| Method | URL | 설명 | 권한 | 응답 | 상태코드 |
|--------|-----|------|------|------|---------|
| GET | `/api/admin/members` | 회원 목록 | ADMIN | `Page<MemberResponse>` | 200 |
| GET | `/api/admin/members/{id}` | 회원 상세 | ADMIN | `MemberResponse` | 200 |
| PATCH | `/api/admin/members/{id}/role` | 권한 변경 | ADMIN | `MemberResponse` | 200 |
| DELETE | `/api/admin/members/{id}` | 회원 삭제 | ADMIN | — | 204 |

#### 업주 API

| Method | URL | 설명 | 권한 | 응답 | 상태코드 |
|--------|-----|------|------|------|---------|
| POST | `/api/owners` | 업주 등록 | ADMIN | `OwnerResponse` | 201 |
| GET | `/api/owners` | 업주 검색 | ALL | `Page<OwnerResponse>` | 200 |
| GET | `/api/owners/{id}` | 업주 상세 | ALL | `OwnerDetailResponse` | 200 |
| PUT | `/api/owners/{id}` | 업주 수정 | ADMIN | `OwnerResponse` | 200 |
| DELETE | `/api/owners/{id}` | 업주 삭제 | ADMIN | — | 204 |
| GET | `/api/owners/{id}/orders` | 업주 주문 | ALL | `Page<OrderResponse>` | 200 |
| GET | `/api/owners/{id}/settles` | 업주 지급 | ALL | `Page<SettleResponse>` | 200 |

#### 주문 API

| Method | URL | 설명 | 권한 | 응답 | 상태코드 |
|--------|-----|------|------|------|---------|
| POST | `/api/orders` | 주문 등록 | ADMIN | `OrderResponse` | 201 |
| GET | `/api/orders` | 주문 검색 | ALL | `Page<OrderResponse>` | 200 |
| GET | `/api/orders/{id}` | 주문 상세 | ALL | `OrderResponse` (details 포함) | 200 |
| PUT | `/api/orders/{id}` | 주문 수정 | ADMIN | `OrderResponse` | 200 |
| DELETE | `/api/orders/{id}` | 주문 삭제 | ADMIN | — | 204 |

#### 주문상세 API

| Method | URL | 설명 | 권한 | 응답 | 상태코드 |
|--------|-----|------|------|------|---------|
| POST | `/api/orders/{orderId}/details` | 상세 등록 | ADMIN | `OrderDetailResponse` | 201 |
| GET | `/api/orders/{orderId}/details` | 상세 조회 | ALL | `List<OrderDetailResponse>` | 200 |
| PUT | `/api/orders/{orderId}/details/{id}` | 상세 수정 | ADMIN | `OrderDetailResponse` | 200 |
| DELETE | `/api/orders/{orderId}/details/{id}` | 상세 삭제 | ADMIN | — | 204 |

#### 보상금 API

| Method | URL | 설명 | 권한 | 응답 | 상태코드 |
|--------|-----|------|------|------|---------|
| POST | `/api/rewards` | 보상 등록 | ADMIN | `RewardResponse` | 201 |
| GET | `/api/rewards` | 보상 검색 | ALL | `Page<RewardResponse>` | 200 |
| GET | `/api/rewards/{id}` | 보상 상세 | ALL | `RewardResponse` | 200 |
| PUT | `/api/rewards/{id}` | 보상 수정 | ADMIN | `RewardResponse` | 200 |
| DELETE | `/api/rewards/{id}` | 보상 삭제 | ADMIN | — | 204 |

#### 지급 API

| Method | URL | 설명 | 권한 | 응답 | 상태코드 |
|--------|-----|------|------|------|---------|
| POST | `/api/settles` | 지급 생성 | ADMIN | `SettleResponse` | 201 |
| GET | `/api/settles` | 지급 검색 | ALL | `Page<SettleResponse>` | 200 |
| GET | `/api/settles/{id}` | 지급 상세 | ALL | `SettleDetailResponse` | 200 |
| PATCH | `/api/settles/{id}/request` | 지급 요청 | ADMIN | `SettleResponse` | 200 |
| PATCH | `/api/settles/{id}/approve` | 지급 승인 | ADMIN | `SettleResponse` | 200 |
| PATCH | `/api/settles/{id}/complete` | 지급 완료 | ADMIN | `SettleResponse` | 200 |
| PATCH | `/api/settles/{id}/reject` | 지급 반려 | ADMIN | `SettleResponse` | 200 |
| DELETE | `/api/settles/{id}` | 지급 삭제 | ADMIN | — | 204 |

### 5.3 요청/응답 DTO 설계

Java 21의 `record`를 활용하여 불변(immutable) DTO를 정의합니다.

```java
// 요청 DTO — Bean Validation 어노테이션
public record SettleCreateRequest(
    @NotNull Long ownerId,
    @NotNull LocalDate settleStartDate,
    @NotNull LocalDate settleEndDate
) {}

// 응답 DTO — 엔티티에서 변환
public record SettleResponse(
    Long id,
    Long ownerId,
    String ownerBusinessName,
    LocalDate settleStartDate,
    LocalDate settleEndDate,
    BigDecimal totalOrderAmount,
    BigDecimal totalFeeAmount,
    BigDecimal totalRewardAmount,
    BigDecimal settlementAmount,
    String status,
    String rejectionReason,
    LocalDateTime createdAt
) {
    public static SettleResponse from(Settle settle) {
        return new SettleResponse(
            settle.getId(),
            settle.getOwner().getId(),
            settle.getOwner().getBusinessName(),
            // ... 매핑
        );
    }
}

// 검색 조건 DTO
public record SettleSearchCondition(
    String ownerKeyword,
    LocalDate startDate,
    LocalDate endDate,
    SettleStatus status,
    int page,
    int size
) {}
```

### 5.4 공통 응답 래퍼

```java
public record ApiResponse<T>(
    boolean success,
    T data,
    String message,
    String errorCode
) {
    public ApiResponse {
        if (success) {
            if (message != null || errorCode != null) {
                throw new IllegalArgumentException("Success response must not contain message or errorCode");
            }
        } else {
            if (data != null) {
                throw new IllegalArgumentException("Error response must not contain data");
            }
            if (message == null || message.isBlank()) {
                throw new IllegalArgumentException("Error response must contain message");
            }
            if (errorCode == null || errorCode.isBlank()) {
                throw new IllegalArgumentException("Error response must contain errorCode");
            }
        }
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(false, null, message, errorCode);
    }
}
```

### 5.5 에러 코드 체계

```java
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

    // 지급
    SETTLE_NOT_FOUND("S001", "존재하지 않는 지급 내역입니다.", 404),
    DUPLICATE_SETTLE("S002", "동일 업주/기간의 정산이 이미 존재합니다.", 409),
    INVALID_STATE_TRANSITION("S003", "허용되지 않는 상태 전이입니다.", 400),
    SETTLE_NOT_DELETABLE("S004", "대기 상태의 지급만 삭제할 수 있습니다.", 400);

    private final String code;
    private final String message;
    private final int httpStatus;
}
```

---

## 6. 핵심 비즈니스 로직 설계

### 6.1 지급 금액 산출 알고리즘

```
[SettleService.createSettle(ownerId, startDate, endDate)]
    |
    +-- 1. 검증
    |   +-- Owner 존재 확인
    |   +-- 동일 업주/기간 중복 정산 확인
    |   +-- startDate <= endDate 확인
    |
    +-- 2. 금액 산출 (SettlementCalculator에 위임)
    |   |
    |   +-- [MyBatis: SettlementMapper.calculateOrderSummary]
    |   |   SELECT SUM(od.payment_amount) AS totalOrderAmount,
    |   |          SUM(od.fee_amount) AS totalFeeAmount
    |   |   FROM order_detail od
    |   |   JOIN orders o ON od.order_id = o.id
    |   |   WHERE o.owner_id = #{ownerId}
    |   |     AND o.status = 'COMPLETED'
    |   |     AND o.order_date BETWEEN #{startDate} AND #{endDate}
    |   |
    |   +-- [MyBatis: SettlementMapper.calculateRewardAmount]
    |   |   SELECT COALESCE(SUM(r.reward_amount), 0)
    |   |   FROM reward r
    |   |   WHERE r.owner_id = #{ownerId}
    |   |     AND r.settled = false
    |   |     AND r.reward_date BETWEEN #{startDate} AND #{endDate}
    |   |
    |   +-- settlementAmount = (totalOrderAmount - totalFeeAmount) + totalRewardAmount
    |       [모든 연산은 BigDecimal.add/subtract 사용]
    |
    +-- 3. Settle 엔티티 생성 (status = PENDING)
    |
    +-- 4. 포함된 Reward의 settled 플래그를 true로 변경
    |
    +-- 5. 저장 및 응답 반환
```

### 6.2 지급 상태 머신 구현

SettleStatus Enum 내부에 상태 전이 규칙을 캡슐화합니다 (3.4절 참조).

```
[상태 전이 시퀀스]

request():   PENDING   ---> REQUESTED    (전이 조건: 없음)
approve():   REQUESTED ---> APPROVED     (전이 조건: 없음)
complete():  APPROVED  ---> COMPLETED    (전이 조건: 없음)
reject():    REQUESTED ---> REJECTED     (전이 조건: rejectionReason 필수)

[불가능한 전이 시 BusinessException(INVALID_STATE_TRANSITION) 발생]
- PENDING   ---> APPROVED   (요청을 건너뛸 수 없음)
- PENDING   ---> COMPLETED  (완료로 직접 전이 불가)
- COMPLETED ---> *           (최종 상태)
- REJECTED  ---> *           (최종 상태)
```

### 6.3 데이터 정합성 검증

| 검증 항목 | 검증 시점 | 검증 방법 | 실패 시 |
|-----------|----------|-----------|---------|
| OrderDetail 합계 = Order totalAmount | 주문 생성/수정 시 | `Order.validateTotalAmount()` | `BusinessException (OR002)` |
| 금액 필드 BigDecimal 사용 | 컴파일 타임 | 필드 타입 강제 | 컴파일 에러 |
| 동일 업주/기간 중복 정산 | 지급 생성 시 | `SettleRepository` 조회 | `BusinessException (S002)` |
| 정산 포함 보상금 수정 방지 | 보상금 수정/삭제 시 | `Reward.isModifiable()` | `BusinessException (R002)` |
| 삭제 가능 상태 확인 | 지급 삭제 시 | `Settle.isDeletable()` | `BusinessException (S004)` |

### 6.4 트랜잭션 경계 설계

| 메서드 | 트랜잭션 | 이유 |
|--------|---------|------|
| `SettleService.createSettle()` | `@Transactional` | 금액 산출 + Settle 저장 + Reward settled 플래그 업데이트가 원자적이어야 함 |
| `SettleService.approve()` | `@Transactional` | 상태 변경의 원자성 보장 |
| `OrderService.createOrder()` | `@Transactional` | Order + OrderDetail 동시 저장 |
| `*Service.search*()` | `@Transactional(readOnly = true)` | 읽기 전용으로 성능 최적화 |

---

## 7. 데이터 액세스 계층 설계

### 7.1 JPA + MyBatis 병행 구조

```
[Service Layer]
    |
    +-- JPA Repository (Spring Data JPA)
    |   +-- save, findById, delete
    |   +-- 메서드 이름 기반 쿼리 (findByEmail, existsByBusinessNumber 등)
    |
    +-- MyBatis Mapper (XML)
        +-- 동적 검색 쿼리 (<if>, <where>, <foreach>)
        +-- 집계 쿼리 (SUM, GROUP BY, JOIN)
        +-- 페이징 쿼리 (LIMIT, OFFSET)
```

### 7.2 JPA Repository 설계

```java
// 각 도메인별 JPA Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndDeletedFalse(String email);
    boolean existsByEmail(String email);
    long countByRoleAndDeletedFalse(MemberRole role);
}

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByIdAndDeletedFalse(Long id);
    boolean existsByBusinessNumber(String businessNumber);
}

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOwnerIdAndStatusAndOrderDateTimeBetween(
        Long ownerId, OrderStatus status, LocalDateTime start, LocalDateTime end);
}

public interface SettleRepository extends JpaRepository<Settle, Long> {
    boolean existsByOwnerIdAndSettleStartDateAndSettleEndDate(
        Long ownerId, LocalDate startDate, LocalDate endDate);
}
```

### 7.3 MyBatis Mapper 설계

```xml
<!-- SettlementMapper.xml — 지급 금액 산출 -->
<select id="calculateOrderSummary" resultType="SettlementSummary">
    SELECT
        COALESCE(SUM(od.payment_amount), 0) AS totalOrderAmount,
        COALESCE(SUM(od.fee_amount), 0) AS totalFeeAmount
    FROM order_detail od
    JOIN orders o ON od.order_id = o.id
    WHERE o.owner_id = #{ownerId}
      AND o.status = 'COMPLETED'
      AND o.order_date_time BETWEEN #{startDate} AND #{endDate}
</select>

<!-- OwnerSearchMapper.xml — 동적 검색 -->
<select id="searchOwners" resultType="OwnerResponse">
    SELECT o.id, o.business_name, o.business_number, o.owner_name, o.phone
    FROM owner o
    WHERE o.deleted = false
    <if test="keyword != null and keyword != ''">
        AND (
            o.business_name LIKE CONCAT('%', #{keyword}, '%')
            OR o.business_number LIKE CONCAT('%', #{keyword}, '%')
            OR o.owner_name LIKE CONCAT('%', #{keyword}, '%')
        )
    </if>
    ORDER BY o.created_at DESC
    LIMIT #{size} OFFSET #{offset}
</select>
```

### 7.4 쿼리 최적화 전략

| 전략 | 적용 대상 | 방법 |
|------|----------|------|
| **인덱스** | 검색 조건 컬럼 | `owner.business_number`, `orders.owner_id + status + order_date_time`, `settle.owner_id + start_date + end_date` |
| **N+1 방지** | 연관 엔티티 조회 | `@EntityGraph` 또는 JPQL `JOIN FETCH`, MyBatis JOIN 쿼리 |
| **페이징** | 목록 조회 | MyBatis `LIMIT/OFFSET`, COUNT 별도 쿼리 |
| **읽기 전용** | 검색/조회 | `@Transactional(readOnly = true)` — Hibernate 더티 체킹 비활성화 |

---

## 8. 프론트엔드 아키텍처

### 8.1 Vue 3 애플리케이션 구조

```
[App.vue]
    |
    +-- [AppLayout.vue]                 # 전체 레이아웃 (사이드바 + 헤더 + 메인)
    |   +-- [AppSidebar.vue]            # 좌측 네비게이션
    |   +-- [AppHeader.vue]             # 상단 헤더 (사용자 정보, 로그아웃)
    |   +-- <router-view />             # 페이지 컨텐츠
    |
    +-- [LoginView.vue]                 # 로그인 (레이아웃 없음)
```

### 8.2 상태 관리 설계 (Pinia)

```
[Pinia Stores]
    |
    +-- authStore
    |   +-- state: { user, isAuthenticated, isAdmin }
    |   +-- actions: { login, logout, fetchMe }
    |   +-- getters: { currentUser, hasAdminRole }
    |
    +-- ownerStore
    |   +-- state: { owners, currentOwner, loading, pagination }
    |   +-- actions: { searchOwners, getOwner, createOwner, updateOwner }
    |
    +-- settleStore
        +-- state: { settles, currentSettle, loading, pagination }
        +-- actions: { searchSettles, getSettle, createSettle, requestSettle, ... }
```

### 8.3 라우팅 및 네비게이션 가드

```javascript
const routes = [
    { path: '/login',     component: LoginView,     meta: { public: true } },
    {
        path: '/',
        component: AppLayout,
        meta: { requiresAuth: true },
        children: [
            { path: '',           component: DashboardView },
            { path: 'owners',     component: OwnerListView },
            { path: 'owners/:id', component: OwnerDetailView },
            { path: 'orders',     component: OrderListView },
            { path: 'rewards',    component: RewardListView },
            { path: 'settles',    component: SettleListView },
            { path: 'settles/:id',component: SettleDetailView },
            { path: 'admin/members', component: MemberListView, meta: { adminOnly: true } },
        ]
    }
];

// 네비게이션 가드
router.beforeEach(async (to) => {
    const auth = useAuthStore();
    if (to.meta.requiresAuth && !auth.isAuthenticated) return '/login';
    if (to.meta.adminOnly && !auth.isAdmin) return '/';
});
```

### 8.4 API 통신 계층

```javascript
// api/client.js — Axios 인스턴스
const client = axios.create({
    baseURL: '/api',
    withCredentials: true,          // 세션 쿠키 전송
    xsrfCookieName: 'XSRF-TOKEN',  // CSRF 쿠키명
    xsrfHeaderName: 'X-XSRF-TOKEN' // CSRF 헤더명
});

// 응답 인터셉터: 401시 로그인 페이지로 리다이렉트
client.interceptors.response.use(
    res => res,
    err => {
        if (err.response?.status === 401) router.push('/login');
        return Promise.reject(err);
    }
);
```

### 8.5 컴포넌트 설계

#### 공통 컴포넌트

| 컴포넌트 | 역할 | Props |
|----------|------|-------|
| `AppTable` | 데이터 테이블 | columns, data, loading |
| `AppPagination` | 페이지네이션 | page, totalPages, onChange |
| `AppModal` | 모달 다이얼로그 | visible, title, onClose |
| `AppSearchBar` | 검색 + 필터 | filters, onSearch |
| `StatusBadge` | 상태 뱃지 (색상) | status, type |
| `ConfirmDialog` | 확인/취소 대화상자 | message, onConfirm |
| `AmountDisplay` | 금액 포맷 표시 | amount, currency |

---

## 9. 횡단 관심사

### 9.1 예외 처리 아키텍처

```
[Controller]
    | 예외 발생
    v
[GlobalExceptionHandler (@RestControllerAdvice)]
    |
    +-- BusinessException (ErrorCode 래핑)
    |       ErrorCode.httpStatus에 따라 HTTP 상태코드 매핑
    |       예: OWNER_NOT_FOUND(404), DUPLICATE_EMAIL(409), INVALID_STATE_TRANSITION(400)
    |
    +-- MethodArgumentNotValidException --> 400 (Bean Validation)
    +-- AccessDeniedException           --> 403
    +-- AuthenticationException         --> 401
    +-- Exception (최종 폴백)            --> 500
    |
    v
[ApiResponse.error(message, errorCode)] --> JSON 응답
```

### 9.2 로깅 전략

| 레벨 | 대상 | 예시 |
|------|------|------|
| **ERROR** | 시스템 장애, 예상치 못한 예외 | DB 연결 실패, NullPointerException |
| **WARN** | 비즈니스 규칙 위반, 잠재적 문제 | 잘못된 상태 전이 시도, 중복 요청 |
| **INFO** | 핵심 비즈니스 이벤트 | 지급 생성, 상태 변경, 로그인 성공 |
| **DEBUG** | 개발 중 디버깅 정보 | SQL 쿼리 파라미터, 메서드 진입/종료 |

### 9.3 유효성 검증 계층

```
[1단계: Vue 프론트엔드]      -- UX 용도. 즉시 피드백
    | (보안 검증 아님)
    v
[2단계: Controller @Valid]   -- Bean Validation (@NotNull, @Size, @Email 등)
    | (형식 검증)
    v
[3단계: Service 비즈니스 검증] -- 도메인 규칙 (중복 확인, 금액 정합성, 상태 검증)
    | (비즈니스 검증)
    v
[4단계: DB 제약 조건]         -- UNIQUE, NOT NULL, FK (최종 안전망)
```

### 9.4 Auditing 설계

```java
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter(Authentication::isAuthenticated)
            .filter(auth -> !(auth instanceof AnonymousAuthenticationToken))
            .map(Authentication::getName)
            .or(() -> Optional.of("SYSTEM"));
    }
}
```

### 9.5 보안 방어 아키텍처

PRD에 정의된 보안 요구사항의 아키텍처 레벨 구현 설계입니다.

#### SQL 인젝션 방어

| 계층 | 방어 기법 | 구현 |
|------|-----------|------|
| **JPA** | Prepared Statement | JPQL/Criteria API 사용 시 자동 파라미터 바인딩 |
| **MyBatis** | `#{}` 바인딩 | XML 매퍼에서 `#{}` 필수. `${}` 사용 절대 금지 |
| **Native Query** | 파라미터 바인딩 | `@Query`에 `:param` 바인딩. 문자열 연결 금지 |

#### XSS (Cross-Site Scripting) 방어

| 계층 | 방어 기법 | 구현 |
|------|-----------|------|
| **Vue 3** | 자동 이스케이핑 | 템플릿 `{{ }}` 바인딩은 자동 HTML 이스케이프. `v-html` 사용 금지 |
| **Spring** | CSP 헤더 | `Content-Security-Policy` 응답 헤더 설정 (`WebMvcConfig`) |
| **입력 검증** | Bean Validation | 서버 측 입력값 길이/패턴 제한으로 악의적 스크립트 주입 차단 |

#### 쿠키 보안 설정

```java
// application.yml
server:
  servlet:
    session:
      cookie:
        http-only: true        # JavaScript 접근 차단
        same-site: lax         # 크로스 사이트 전송 제한
        secure: false          # 개발 환경 (운영 시 true)
      timeout: 30m             # 세션 타임아웃
```

---

## 10. 데이터베이스 설계

### 10.1 테이블 정의서

#### member

| 컬럼 | 타입 | NULL | 기본값 | 설명 |
|------|------|:---:|-------|------|
| id | BIGINT | NO | AUTO_INCREMENT | PK |
| email | VARCHAR(100) | NO | | UNIQUE |
| password | VARCHAR(255) | NO | | BCrypt 해시 |
| name | VARCHAR(20) | NO | | |
| role | VARCHAR(10) | NO | 'USER' | ADMIN/USER |
| deleted | BOOLEAN | NO | false | |
| created_at | DATETIME(6) | NO | | |
| updated_at | DATETIME(6) | NO | | |
| created_by | VARCHAR(100) | NO | | |
| updated_by | VARCHAR(100) | YES | | |

#### owner

| 컬럼 | 타입 | NULL | 기본값 | 설명 |
|------|------|:---:|-------|------|
| id | BIGINT | NO | AUTO_INCREMENT | PK |
| business_name | VARCHAR(100) | NO | | |
| business_number | VARCHAR(12) | NO | | UNIQUE |
| owner_name | VARCHAR(20) | NO | | |
| phone | VARCHAR(20) | NO | | |
| email | VARCHAR(100) | YES | | |
| address | VARCHAR(200) | YES | | |
| bank_name | VARCHAR(50) | YES | | |
| account_number | VARCHAR(50) | YES | | |
| deleted | BOOLEAN | NO | false | |
| created_at ~ updated_by | (BaseEntity) | | | |

#### orders

| 컬럼 | 타입 | NULL | 기본값 | 설명 |
|------|------|:---:|-------|------|
| id | BIGINT | NO | AUTO_INCREMENT | PK |
| owner_id | BIGINT | NO | | FK → owner |
| order_date_time | DATETIME(6) | NO | | |
| status | VARCHAR(20) | NO | | RECEIVED/COMPLETED/CANCELLED |
| total_amount | DECIMAL(15,2) | NO | | |
| created_at ~ updated_by | (BaseEntity) | | | |

#### order_detail

| 컬럼 | 타입 | NULL | 기본값 | 설명 |
|------|------|:---:|-------|------|
| id | BIGINT | NO | AUTO_INCREMENT | PK |
| order_id | BIGINT | NO | | FK → orders (CASCADE DELETE) |
| payment_method | VARCHAR(20) | NO | | |
| payment_amount | DECIMAL(15,2) | NO | | |
| fee_amount | DECIMAL(15,2) | NO | | |
| created_at ~ updated_by | (BaseEntity) | | | |

#### reward

| 컬럼 | 타입 | NULL | 기본값 | 설명 |
|------|------|:---:|-------|------|
| id | BIGINT | NO | AUTO_INCREMENT | PK |
| owner_id | BIGINT | NO | | FK → owner |
| reward_amount | DECIMAL(15,2) | NO | | |
| reason | VARCHAR(30) | NO | | |
| reason_detail | VARCHAR(500) | NO | | |
| reward_date_time | DATETIME(6) | NO | | |
| settled | BOOLEAN | NO | false | 정산 포함 여부 |
| created_at ~ updated_by | (BaseEntity) | | | |

#### settle

| 컬럼 | 타입 | NULL | 기본값 | 설명 |
|------|------|:---:|-------|------|
| id | BIGINT | NO | AUTO_INCREMENT | PK |
| owner_id | BIGINT | NO | | FK → owner |
| settle_start_date | DATE | NO | | |
| settle_end_date | DATE | NO | | |
| total_order_amount | DECIMAL(15,2) | NO | | |
| total_fee_amount | DECIMAL(15,2) | NO | | |
| total_reward_amount | DECIMAL(15,2) | NO | | |
| settlement_amount | DECIMAL(15,2) | NO | | 최종 지급 금액 |
| status | VARCHAR(20) | NO | 'PENDING' | |
| rejection_reason | VARCHAR(500) | YES | | |
| created_at ~ updated_by | (BaseEntity) | | | |

### 10.2 인덱스 전략

| 테이블 | 인덱스명 | 컬럼 | 유형 | 용도 |
|--------|---------|------|------|------|
| member | UK_member_email | email | UNIQUE | 로그인 조회, 중복 검증 |
| owner | UK_owner_business_no | business_number | UNIQUE | 중복 검증 |
| owner | IDX_owner_business_name | business_name | INDEX | 상호명 검색 |
| orders | IDX_orders_owner_status_date | owner_id, status, order_date_time | COMPOSITE | 정산 집계 쿼리 (핵심) |
| order_detail | IDX_order_detail_order_id | order_id | INDEX | 주문상세 조회 (FK) |
| reward | IDX_reward_owner_settled_date | owner_id, settled, reward_date_time | COMPOSITE | 정산 보상금 집계 |
| settle | IDX_settle_owner_period | owner_id, settle_start_date, settle_end_date | COMPOSITE | 중복 정산 검증 |
| settle | IDX_settle_status | status | INDEX | 상태 필터 검색 |

### 10.3 데이터 마이그레이션

개발 환경에서는 `spring.jpa.hibernate.ddl-auto=create-drop` 사용. 운영 환경 이관 시 Flyway 도입을 권장합니다.

```
src/main/resources/db/migration/
+-- V1__create_member.sql
+-- V2__create_owner.sql
+-- V3__create_orders_and_detail.sql
+-- V4__create_reward.sql
+-- V5__create_settle.sql
+-- V6__create_indexes.sql
+-- V7__insert_seed_data.sql
```

### 10.4 시드 데이터

개발/테스트용 초기 데이터를 `data.sql`로 제공합니다.

| 데이터 | 건수 | 내용 |
|--------|------|------|
| 관리자 계정 | 1건 | admin@payline.com / Admin123! |
| 일반회원 계정 | 1건 | user@payline.com / User123! |
| 업주 | 5건 | 행복치킨, 맛있는분식, 피자마을, 커피하우스, 초밥천국 |
| 주문 | 50건 | 각 업주별 10건 (7 COMPLETED, 2 RECEIVED, 1 CANCELLED) |
| 주문상세 | 80건 | 주문당 1~3건 |
| 보상금 | 5건 | 일부 업주 대상 |
| 지급 | 3건 | PENDING, REQUESTED, COMPLETED 각 1건 |

---

> **문서 이력**
>
> | 버전 | 날짜 | 변경 내용 |
> |------|------|-----------|
> | v1.0 | 2026-02-12 | 최초 작성. PRD v3.0 기반 전체 아키텍처 설계 |
> | v1.1 | 2026-02-14 | ApiResponse 스니펫을 실제 구현(불변식 검증, 제네릭 error 팩토리) 기준으로 정합화 |
> | v1.2 | 2026-02-14 | 문서 메타데이터(버전/최종 수정일)와 이력 정합성 보정 |
> | v1.3 | 2026-02-14 | 코드 기준 전체 정합성 보정: BusinessException concrete화, BaseEntity 2계층 구조, SettleStatus switch expression, 예외 계층 다이어그램 |
