# 📋 WBS & GitHub Issue 템플릿: 페이라인 (PayLine)

### 작업 분해 구조 + 프로젝트 관리 가이드

> _PRD v3.0.1 / 아키텍처 설계서 v1.2 기반_

---

| 항목 | 내용 |
|------|------|
| **문서 유형** | WBS (Work Breakdown Structure) |
| **문서 버전** | v1.1 |
| **최종 수정일** | 2026년 2월 14일 |
| **총 개발 기간** | 6주 (30 영업일) |
| **총 태스크** | 81개 (필수 76 + 선택 5) |
| **마일스톤** | 6개 (주 단위) |

---

## 목차

1. [마일스톤 개요](#1-마일스톤-개요)
2. [Week 1 — 프로젝트 기반 + 인증/회원](#2-week-1--프로젝트-기반--인증회원)
3. [Week 2 — 업주 + 주문 도메인](#3-week-2--업주--주문-도메인)
4. [Week 3 — 보상금 + 지급 도메인](#4-week-3--보상금--지급-도메인)
5. [Week 4 — Vue 프론트엔드 + API 연동](#5-week-4--vue-프론트엔드--api-연동)
6. [Week 5 — 테스트 + Phase 2 기능](#6-week-5--테스트--phase-2-기능)
7. [Week 6 — 마무리 + 포트폴리오 정리](#7-week-6--마무리--포트폴리오-정리)
8. [GitHub Issue 템플릿](#8-github-issue-템플릿)
9. [GitHub 라벨 체계](#9-github-라벨-체계)
10. [브랜치 전략 상세](#10-브랜치-전략-상세)

---

## 1. 마일스톤 개요

```
M1 (Week 1)    M2 (Week 2)    M3 (Week 3)    M4 (Week 4)    M5 (Week 5)    M6 (Week 6)
[기반+인증] --> [업주+주문] --> [보상+지급] --> [프론트엔드] --> [테스트+P2] --> [마무리]
  17 tasks       15 tasks       10 tasks       13 tasks       12 tasks       14 tasks
```

| 마일스톤 | 기간 | 핵심 목표 | 완료 기준 |
|----------|------|-----------|-----------|
| **M1** | Day 1~5 | 프로젝트 뼈대 + 세션 인증 + Member CRUD | 로그인/로그아웃 동작, 회원 CRUD API 응답 확인 |
| **M2** | Day 6~10 | Owner CRUD+검색, Order/OrderDetail CRUD | Swagger에서 업주/주문 API 전체 테스트 가능 |
| **M3** | Day 11~15 | Reward CRUD, Settle 생성+상태머신 | 지급 금액 자동 산출 + 상태 전이 동작 확인 |
| **M4** | Day 16~20 | Vue 3 SPA 전체 페이지 + API 연동 | 브라우저에서 전체 워크플로우 동작 |
| **M5** | Day 21~25 | 단위/통합 테스트 80%+, 대시보드, Swagger | JaCoCo 80%, Swagger UI 완성 |
| **M6** | Day 26~30 | 리팩토링, README, Docker(선택), 포트폴리오 | GitHub 리포지토리 포트폴리오 완성 |

---

## 2. Week 1 — 프로젝트 기반 + 인증/회원

### Day 1~2: 프로젝트 초기 설정

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W1-01 | Gradle 프로젝트 생성 (Spring Boot 4.0.2, Java 21) | 1h | `chore` | `chore/init-project` |
| W1-02 | build.gradle 의존성 설정 (Security, JPA, MyBatis, Lombok, H2, MySQL, SpringDoc, Validation) | 1h | `chore` | `chore/init-project` |
| W1-03 | application.yml 프로파일 분리 (default, dev, prod) | 1h | `chore` | `chore/init-project` |
| W1-04 | BaseEntity, BaseTimeEntity 생성 + JPA Auditing 설정 | 1h | `feat` | `feat/global-config` |
| W1-05 | ApiResponse, PageResponse 공통 DTO 생성 | 1h | `feat` | `feat/global-config` |
| W1-06 | ErrorCode Enum + BusinessException 계층 + GlobalExceptionHandler 구현 | 2h | `feat` | `feat/global-error` |

### Day 3~4: 인증/인가

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W1-07 | Member Entity + MemberRole Enum 생성 | 1h | `feat` | `feat/member-domain` |
| W1-08 | MemberRepository (JPA) 생성 | 0.5h | `feat` | `feat/member-domain` |
| W1-09 | SecurityConfig 작성 (세션, CSRF, 인가 규칙, 핸들러) | 3h | `feat` | `feat/auth-session` |
| W1-10 | CustomUserDetailsService + CustomUserDetails 구현 | 1.5h | `feat` | `feat/auth-session` |
| W1-11 | LoginSuccessHandler, LoginFailureHandler (JSON 응답) 구현 | 1.5h | `feat` | `feat/auth-session` |
| W1-12 | JpaAuditingConfig의 AuditorAware Bean 구성 (세션에서 사용자 추출) | 0.5h | `feat` | `feat/auth-session` |

### Day 5: 회원 관리

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W1-13 | MemberService (가입, 조회, 수정, 삭제, 권한변경) 구현 | 3h | `feat` | `feat/member-service` |
| W1-14 | SignupRequest, MemberResponse, MemberUpdateRequest (record) DTO 생성 | 1h | `feat` | `feat/member-service` |
| W1-15 | MemberController (/api/members) 구현 | 1.5h | `feat` | `feat/member-api` |
| W1-16 | AdminMemberController (/api/admin/members) 구현 | 1.5h | `feat` | `feat/member-api` |
| W1-17 | 시드 데이터 (data.sql) — admin/user 계정 생성 | 0.5h | `chore` | `chore/seed-data` |

> **M1 완료 기준 체크리스트:**
> - [ ] POST /api/members/signup → 201
> - [ ] POST /api/login (email/password) → 200 + JSESSIONID 쿠키
> - [ ] GET /api/members/me → 현재 사용자 정보
> - [ ] POST /api/logout → 세션 무효화
> - [ ] GET /api/admin/members → ADMIN만 접근
> - [ ] 비인증 /api/** → 401, USER의 /api/admin/** → 403

---

## 3. Week 2 — 업주 + 주문 도메인

### Day 6~7: 업주 관리

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W2-01 | Owner Entity 생성 (연관관계 포함) | 1h | `feat` | `feat/owner-domain` |
| W2-02 | OwnerRepository (JPA) 생성 | 0.5h | `feat` | `feat/owner-domain` |
| W2-03 | OwnerSearchMapper.xml (MyBatis 동적 검색) 작성 | 2h | `feat` | `feat/owner-search` |
| W2-04 | OwnerService 구현 (CRUD + 검색 + soft delete) | 2.5h | `feat` | `feat/owner-service` |
| W2-05 | Owner DTO (Create/Update/Response/Detail/SearchCondition) 생성 | 1h | `feat` | `feat/owner-service` |
| W2-06 | OwnerController 구현 + 업주별 하위 리소스 | 2h | `feat` | `feat/owner-api` |

### Day 8~10: 주문 관리

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W2-07 | Order Entity + OrderStatus Enum 생성 | 1h | `feat` | `feat/order-domain` |
| W2-08 | OrderDetail Entity + PaymentMethod Enum 생성 | 1h | `feat` | `feat/order-domain` |
| W2-09 | OrderRepository, OrderDetailRepository (JPA) 생성 | 0.5h | `feat` | `feat/order-domain` |
| W2-10 | OrderSearchMapper.xml (MyBatis) 작성 | 1.5h | `feat` | `feat/order-search` |
| W2-11 | OrderService 구현 (CRUD + 금액 정합성 검증 + 상태 관리) | 3h | `feat` | `feat/order-service` |
| W2-12 | OrderDetailService 구현 | 1.5h | `feat` | `feat/order-service` |
| W2-13 | Order/OrderDetail DTO 생성 | 1h | `feat` | `feat/order-service` |
| W2-14 | OrderController, OrderDetailController 구현 | 2h | `feat` | `feat/order-api` |
| W2-15 | 시드 데이터 확장 — 업주 5건, 주문 50건, 주문상세 80건 | 1h | `chore` | `chore/seed-data-v2` |

> **M2 완료 기준 체크리스트:**
> - [ ] 업주 CRUD + 상호명/사업자번호/대표자명 검색 동작
> - [ ] 사업자번호 중복 시 409 응답
> - [ ] 주문 CRUD + OrderDetail 동시 생성
> - [ ] OrderDetail 합계 ≠ totalAmount 시 400 응답
> - [ ] 업주별 주문 조회 (/api/owners/{id}/orders) 동작

---

## 4. Week 3 — 보상금 + 지급 도메인

### Day 11~12: 보상금 관리

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W3-01 | Reward Entity + RewardReason Enum 생성 | 1h | `feat` | `feat/reward-domain` |
| W3-02 | RewardRepository (JPA) + RewardSearchMapper.xml (MyBatis) | 1.5h | `feat` | `feat/reward-domain` |
| W3-03 | RewardService 구현 (CRUD + settled 플래그 검증) | 2h | `feat` | `feat/reward-service` |
| W3-04 | Reward DTO 생성 + RewardController 구현 | 2h | `feat` | `feat/reward-api` |

### Day 13~15: 지급 관리 (핵심)

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W3-05 | Settle Entity + SettleStatus Enum (상태 전이 로직 포함) 생성 | 2h | `feat` | `feat/settle-domain` |
| W3-06 | SettleRepository (JPA) + SettleSearchMapper.xml (MyBatis) | 1.5h | `feat` | `feat/settle-domain` |
| W3-07 | SettlementMapper.xml (집계 쿼리 — 주문 합계, 보상 합계) | 2h | `feat` | `feat/settle-calc` |
| W3-08 | SettlementCalculator 구현 (금액 산출 전담 클래스) | 2h | `feat` | `feat/settle-calc` |
| W3-09 | SettleService 구현 (생성 + 상태 전이 + 삭제 + Reward settled 연동) | 3h | `feat` | `feat/settle-service` |
| W3-10 | Settle DTO 생성 + SettleController 구현 (상태 전이 엔드포인트 포함) | 2.5h | `feat` | `feat/settle-api` |

> **M3 완료 기준 체크리스트:**
> - [ ] 보상금 CRUD 동작, settled=true인 보상금 수정 시 400
> - [ ] POST /api/settles → 금액 자동 산출 (BigDecimal)
> - [ ] PENDING → REQUESTED → APPROVED → COMPLETED 순차 전이
> - [ ] REQUESTED → REJECTED (반려 사유 필수)
> - [ ] 잘못된 상태 전이 시 400 응답
> - [ ] 동일 업주/기간 중복 생성 시 409 응답
> - [ ] 지급 상세 조회 시 포함된 주문/보상 목록 반환

---

## 5. Week 4 — Vue 프론트엔드 + API 연동

### Day 16~17: Vue 프로젝트 셋업 + 공통

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W4-01 | Vue 3 + Vite 프로젝트 생성 (Pinia, Vue Router, Axios) | 1h | `feat` | `feat/frontend-init` |
| W4-02 | vite.config.js 프록시 설정 (/api → localhost:8080) | 0.5h | `feat` | `feat/frontend-init` |
| W4-03 | Axios 인스턴스 (CSRF 토큰 인터셉터, 401 리다이렉트) | 1.5h | `feat` | `feat/frontend-api` |
| W4-04 | AppLayout, AppHeader, AppSidebar 레이아웃 컴포넌트 | 2h | `feat` | `feat/frontend-layout` |
| W4-05 | 공통 컴포넌트 (AppTable, AppPagination, AppModal, StatusBadge) | 3h | `feat` | `feat/frontend-components` |
| W4-06 | Vue Router 설정 + 네비게이션 가드 (인증/관리자 체크) | 1.5h | `feat` | `feat/frontend-router` |

### Day 18~19: 핵심 페이지

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W4-07 | LoginView + authStore (Pinia) | 2h | `feat` | `feat/frontend-auth` |
| W4-08 | OwnerListView + OwnerDetailView + ownerApi | 3h | `feat` | `feat/frontend-owner` |
| W4-09 | OrderListView + orderApi | 2h | `feat` | `feat/frontend-order` |
| W4-10 | RewardListView + rewardApi | 1.5h | `feat` | `feat/frontend-reward` |

### Day 20: 지급 페이지 + 통합

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W4-11 | SettleListView + SettleDetailView + settleApi (상태 전이 버튼 포함) | 3h | `feat` | `feat/frontend-settle` |
| W4-12 | AdminMemberListView (관리자 전용) | 1.5h | `feat` | `feat/frontend-admin` |
| W4-13 | 전체 플로우 통합 테스트 (브라우저에서 E2E 수동 검증) | 2h | `test` | — |

> **M4 완료 기준 체크리스트:**
> - [ ] 로그인 → 메인 화면 → 각 메뉴 네비게이션 동작
> - [ ] 업주 검색/등록/수정/삭제 UI 동작
> - [ ] 주문 목록/상세 조회 동작
> - [ ] 지급 생성 → 요청 → 승인 → 완료 전체 플로우 UI 동작
> - [ ] 비로그인 시 로그인 페이지 리다이렉트
> - [ ] 일반회원 관리자 메뉴 숨김

---

## 6. Week 5 — 테스트 + Phase 2 기능

### Day 21~23: 테스트 작성

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W5-01 | MemberService 단위 테스트 (가입, 권한변경, 최소관리자 검증) | 2h | `test` | `test/member` |
| W5-02 | OwnerService 단위 테스트 (CRUD, 사업자번호 중복) | 1.5h | `test` | `test/owner` |
| W5-03 | OrderService 단위 테스트 (금액 정합성, 상태 관리) | 2h | `test` | `test/order` |
| W5-04 | RewardService 단위 테스트 (settled 플래그 검증) | 1h | `test` | `test/reward` |
| W5-05 | SettleService 단위 테스트 (금액 산출, 상태 전이, 중복 방지) | 3h | `test` | `test/settle` |
| W5-06 | SettlementCalculator 단위 테스트 (BigDecimal 정밀도) | 1.5h | `test` | `test/settle` |
| W5-07 | 인증/인가 통합 테스트 (401, 403, CSRF, 세션) | 2h | `test` | `test/auth` |
| W5-08 | 주요 API 통합 테스트 (MockMvc — 전체 엔드포인트) | 3h | `test` | `test/integration` |

### Day 24~25: Phase 2 기능

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W5-09 | SpringDoc OpenAPI 설정 + Swagger 어노테이션 추가 | 2h | `feat` | `feat/swagger` |
| W5-10 | DashboardView + 대시보드 집계 API | 3h | `feat` | `feat/dashboard` |
| W5-11 | 글로벌 예외 처리 고도화 (검증 에러 상세 메시지, 필드별 에러) | 1.5h | `refactor` | `refactor/error-handling` |
| W5-12 | JaCoCo 플러그인 추가 + 커버리지 리포트 확인 | 1h | `chore` | `chore/jacoco` |

> **M5 완료 기준 체크리스트:**
> - [ ] JaCoCo Service 계층 커버리지 80% 이상
> - [ ] 보안 테스트 통과 (비인증 차단, 권한 차단, CSRF 차단)
> - [ ] Swagger UI에서 전체 API 확인 가능
> - [ ] 대시보드 차트 렌더링

---

## 7. Week 6 — 마무리 + 포트폴리오 정리

### Day 26~27: 리팩토링

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W6-01 | 코드 리뷰 및 리팩토링 (메서드 분리, 네이밍 개선, 중복 제거) | 3h | `refactor` | `refactor/cleanup` |
| W6-02 | 미사용 import 정리, 코드 포맷팅 통일 | 1h | `style` | `refactor/cleanup` |
| W6-03 | 에러 메시지 한글화 및 통일 | 1h | `refactor` | `refactor/cleanup` |

### Day 28~29: 문서화

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W6-04 | README.md 작성 (프로젝트 소개, 기술 스택, 실행 방법, 스크린샷) | 3h | `docs` | `docs/readme` |
| W6-05 | API 문서 최종 검수 (Swagger 어노테이션 누락 확인) | 1h | `docs` | `docs/swagger-polish` |
| W6-06 | 아키텍처 결정 기록 (ADR) — 세션 선택, JPA+MyBatis, Vue 선택 근거 | 2h | `docs` | `docs/adr` |

### Day 30: 포트폴리오 정리 + 선택적 확장

| # | Task | 예상 시간 | 라벨 | 브랜치 |
|---|------|----------|------|--------|
| W6-07 | GitHub Project 칸반 보드 정리 (모든 Issue Closed 확인) | 1h | `chore` | — |
| W6-08 | GitHub 리포지토리 Description, Topics, About 설정 | 0.5h | `chore` | — |
| W6-09 | (선택) Dockerfile + docker-compose.yml 작성 | 2h | `feat` | `feat/docker` |
| W6-10 | (선택) GitHub Actions CI 워크플로우 (빌드+테스트) | 2h | `feat` | `feat/ci` |
| W6-11 | (선택) 엑셀 다운로드 기능 (정산 내역) | 2h | `feat` | `feat/excel-export` |
| W6-12 | (선택) 감사 로그 기능 | 2h | `feat` | `feat/audit-log` |
| W6-13 | (선택) 일괄 정산 생성 (Virtual Threads 활용) | 3h | `feat` | `feat/batch-settle` |
| W6-14 | 최종 점검 및 릴리스 태그 (v1.0.0) | 1h | `chore` | — |

> **M6 완료 기준 체크리스트:**
> - [ ] README에 프로젝트 실행 방법 명시
> - [ ] Swagger UI 전체 엔드포인트 문서화
> - [ ] GitHub Project 보드 모든 Issue Closed
> - [ ] main 브랜치 빌드 + 전체 테스트 통과

---

## 8. GitHub Issue 템플릿

### 8.1 Feature Issue 템플릿

```markdown
---
name: 기능 구현
about: 새로운 기능을 구현합니다
labels: feat
---

## 📌 개요
<!-- 구현할 기능을 한 줄로 설명 -->

## 📋 상세 요구사항
<!-- PRD/아키텍처 설계서의 관련 섹션 참조 -->
- [ ] 요구사항 1
- [ ] 요구사항 2
- [ ] 요구사항 3

## 🏗️ 구현 범위
- **Entity/Domain:**
- **DTO:**
- **Repository:**
- **Service:**
- **Controller:**

## ✅ 완료 기준 (DoD)
- [ ] 기능 구현 완료
- [ ] 컴파일 에러 없음
- [ ] 기존 테스트 통과
- [ ] Swagger에서 API 동작 확인

## 🔗 관련
- PRD: 섹션 X.X
- 아키텍처: 섹션 X.X
- 관련 Issue: #
```

### 8.2 Bug Fix Issue 템플릿

```markdown
---
name: 버그 수정
about: 발견된 버그를 수정합니다
labels: bug
---

## 🐛 버그 설명
<!-- 어떤 문제가 발생하는지 설명 -->

## 📍 재현 경로
1. ...
2. ...
3. ...

## ✅ 기대 동작
<!-- 정상적으로 동작해야 하는 방식 -->

## 🔧 수정 방안
<!-- 예상되는 원인과 수정 방향 -->

## 🔗 관련
- 관련 Issue: #
```

### 8.3 테스트 Issue 템플릿

```markdown
---
name: 테스트 작성
about: 테스트 코드를 작성합니다
labels: test
---

## 🧪 테스트 대상
- **클래스:** 
- **테스트 유형:** 단위 / 통합 / 보안

## 📋 테스트 케이스
- [ ] TC-01: 
- [ ] TC-02: 
- [ ] TC-03: 

## ✅ 완료 기준
- [ ] 모든 테스트 케이스 통과
- [ ] 커버리지 기준 충족
```

---

## 9. GitHub 라벨 체계

| 라벨 | 색상 | 설명 |
|------|------|------|
| `feat` | `#0E8A16` (녹색) | 새 기능 구현 |
| `bug` | `#D73A4A` (빨강) | 버그 수정 |
| `refactor` | `#FBCA04` (노랑) | 리팩토링 |
| `test` | `#0075CA` (파랑) | 테스트 작성 |
| `docs` | `#C5DEF5` (하늘) | 문서 작업 |
| `chore` | `#BFD4F2` (연파랑) | 빌드/설정 |
| `style` | `#E4E669` (연노랑) | 코드 포맷팅 |
| `domain:member` | `#D4C5F9` (연보라) | 회원 도메인 |
| `domain:owner` | `#D4C5F9` | 업주 도메인 |
| `domain:order` | `#D4C5F9` | 주문 도메인 |
| `domain:reward` | `#D4C5F9` | 보상금 도메인 |
| `domain:settle` | `#D4C5F9` | 지급 도메인 |
| `domain:auth` | `#D4C5F9` | 인증/인가 |
| `domain:frontend` | `#D4C5F9` | Vue 프론트엔드 |
| `priority:high` | `#B60205` (진빨강) | 높은 우선순위 |
| `priority:medium` | `#FBCA04` | 중간 우선순위 |
| `priority:low` | `#0E8A16` | 낮은 우선순위 |
| `optional` | `#CCCCCC` (회색) | 선택적 구현 |

---

## 10. 브랜치 전략 상세

### 브랜치 네이밍 규칙

```
main                          # 항상 배포 가능 상태
  |
  +-- feat/member-domain      # 기능 개발
  +-- feat/auth-session
  +-- feat/settle-service
  +-- feat/frontend-settle
  +-- test/settle             # 테스트 작성
  +-- refactor/cleanup        # 리팩토링
  +-- docs/readme             # 문서
  +-- chore/init-project      # 설정/빌드
```

### PR 워크플로우

```
1. Issue 생성 (#12: feat: 지급 데이터 생성 API 구현)
2. 브랜치 생성 (feat/settle-service)
3. 개발 + 커밋 (feat: 지급 데이터 생성 API 구현 #12)
4. PR 생성 (Closes #12)
5. 셀프 코드 리뷰
6. main에 Merge (Squash & Merge 권장)
7. 브랜치 삭제
```

### 커밋 메시지 규칙

```
<type>: <subject> #<issue-number>

예시:
feat: 지급 데이터 생성 API 구현 #12
fix: BigDecimal 연산 시 scale 누락 수정 #15
test: SettleService 상태 전이 단위 테스트 추가 #18
refactor: SettleService 금액 산출 로직 분리 #22
docs: README 프로젝트 설명 추가 #30
chore: JaCoCo 플러그인 추가 #25
```

---

> **문서 이력**
>
> | 버전 | 날짜 | 변경 내용 |
> |------|------|-----------|
> | v1.0 | 2026-02-12 | 최초 작성. 6주 72태스크 WBS + GitHub 템플릿 |
> | v1.1 | 2026-02-14 | 태스크 수치 재집계(81개) 및 주차별 태스크 통계 정합성 보정 |
