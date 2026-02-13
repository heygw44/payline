# 🧾 PRD: 페이라인 (PayLine)

### 우아한형제들 정산시스템 클론 코딩 — 백엔드 포트폴리오 프로젝트

> _"1원도 틀리면 안 되는 세계에 오신 것을 환영합니다."_

---

| 항목 | 내용 |
|------|------|
| **프로젝트명** | 페이라인 (PayLine) |
| **프로젝트 유형** | 주니어 백엔드 개발자 1인 포트폴리오 프로젝트 |
| **문서 버전** | v3.0 |
| **최종 수정일** | 2026년 2월 11일 |
| **대상 플랫폼** | Web (Desktop 브라우저 최적화) |
| **인증 방식** | 세션(Session) 기반 인증 |
| **프론트엔드** | Vue 3 (Composition API + Vite) |
| **백엔드** | Java 21 + Spring Boot 4.0.2 |
| **데이터 저장소** | MySQL 9.x (운영) / H2 (개발·테스트) |

---

## 목차

1. [상세 제품 설명](#1-상세-제품-설명)
   - 1.1 프로젝트 개요
   - 1.2 프로젝트 목적 및 기대 효과
   - 1.3 프로젝트 범위 정의
   - 1.4 성공 지표
   - 1.5 제약 조건 및 전제 사항
2. [레퍼런스 서비스 및 상세 근거](#2-레퍼런스-서비스-및-상세-근거)
   - 2.1 1차 레퍼런스 (직접 원본)
   - 2.2 2차 레퍼런스 (도메인 이해)
   - 2.3 3차 레퍼런스 (기술 패턴)
   - 2.4 레퍼런스 활용 매트릭스
3. [핵심 기능 및 상세 스펙](#3-핵심-기능-및-상세-스펙)
   - 3.1 시스템 권한 체계
   - 3.2 인증/인가 (Authentication & Authorization)
   - 3.3 어드민 회원 관리 (Member)
   - 3.4 B2B 업주 관리 (Owner)
   - 3.5 주문 관리 (Order / OrderDetail)
   - 3.6 보상 금액 관리 (Reward)
   - 3.7 지급 관리 (Settle)
   - 3.8 공통 기능 요구사항
   - 3.9 API 엔드포인트 설계 요약
   - 3.10 비기능 요구사항 (NFR)
4. [추가 제안 기능](#4-추가-제안-기능)
   - 4.1 Phase 2 — 포트폴리오 차별화 기능
   - 4.2 Phase 3 — 고급 확장 기능
   - 4.3 기능 로드맵
5. [사용자 페르소나 및 시나리오](#5-사용자-페르소나-및-시나리오)
   - 5.1 페르소나 정의
   - 5.2 핵심 사용자 여정 (User Journey)
   - 5.3 상세 사용 시나리오
6. [기술 스택 권장사항](#6-기술-스택-권장사항)
   - 6.1 기술 스택 전체 구성도
   - 6.2 핵심 기술 스택 상세
   - 6.3 세션 기반 인증/인가 아키텍처
   - 6.4 Vue 3 프론트엔드 전략
   - 6.5 JPA + MyBatis 병행 전략
   - 6.6 보안 요구사항
   - 6.7 테스트 전략
   - 6.8 개발 원칙 및 컨벤션
   - 6.9 프로젝트 관리 전략

---

## 1. 상세 제품 설명

### 1.1 프로젝트 개요

**페이라인(PayLine)**는 우아한형제들(배달의민족) 정산시스템팀의 신입 개발자 파일럿 프로젝트를 클론 코딩하는 **주니어 백엔드 개발자 1인 포트폴리오 프로젝트**입니다.

정산(Settlement)이란 배달 플랫폼에서 발생한 주문 데이터를 수집·가공하여, 가맹점(B2B 업주)에게 지급해야 할 금액을 산출하고 관리하는 일련의 비즈니스 프로세스를 의미합니다. 하루 수백만 건의 주문이 발생하는 배달의민족에서 **1원의 오차도 허용하지 않는** 정산시스템은 핀테크 도메인의 핵심이자, 백엔드 개발자에게 가장 높은 수준의 데이터 정합성과 비즈니스 로직 설계 역량을 요구하는 영역입니다.

본 프로젝트는 우아한형제들 기술블로그에 공개된 파일럿 요구사항을 기반으로, 정산 어드민 시스템의 **간략하지만 핵심적인 버전**을 구현합니다. 회원 관리, 업주 관리, 주문 관리, 보상금 관리, 지급 관리의 5대 도메인을 다루며, **Java 21 + Spring Boot 4.0.2** 최신 기술 스택과 **Vue 3** 프론트엔드, 세션 기반 인증/인가 체계를 통해 실무 수준의 어드민 시스템을 완성합니다.

### 1.2 프로젝트 목적 및 기대 효과

| 목적 | 상세 설명 | 기대 효과 |
|------|-----------|-----------|
| **핀테크 도메인 역량 증명** | 금액 계산, 상태 관리, 권한 분리 등 정산 도메인 고유의 비즈니스 로직 구현 | "돈을 다루는 시스템"을 이해하고 구현할 수 있다는 실증적 증거 확보 |
| **최신 기술 스택 역량** | Java 21(Virtual Threads, Record, Pattern Matching) + Spring Boot 4.0.2 + Vue 3 조합 | 2026년 기준 최신 기술 트렌드에 부합하는 포트폴리오 |
| **백엔드 기본기 시연** | OOP 설계, 클린 코드, 계층형 아키텍처, 예외 처리, 로깅 등 실무 필수 기본기 적용 | 코드 리뷰에서 즉시 확인 가능한 백엔드 역량 포트폴리오 |
| **보안 역량 증명** | SQL 인젝션, XSS, CSRF 방어와 세션 기반 인증/인가 구현 | 금융·핀테크 도메인 취업 시 핵심 가점 요소 |
| **테스트 문화 체화** | 단위 테스트, 통합 테스트를 체계적으로 작성하여 코드 신뢰성 보장 | TDD/테스트 작성 역량이 코드와 함께 자연스럽게 드러남 |
| **실제 기업 요구사항 기반 개발** | 실제 기업이 신입에게 부여하는 수준의 과제를 독립적으로 완수 | "이 사람은 입사 후 파일럿 프로젝트를 잘 해낼 수 있겠다"는 확신 제공 |

### 1.3 프로젝트 범위 정의

#### In-Scope (구현 범위)

| 영역 | 구현 항목 |
|------|-----------|
| **인증/인가** | 세션 기반 로그인/로그아웃, 역할(ADMIN/USER) 기반 접근 제어, 회원가입 |
| **회원 관리 (Member)** | CRUD + 권한 관리. 관리자 전용 회원 목록 조회 |
| **업주 관리 (Owner)** | CRUD + 검색. 업주-주문 간 연관관계 |
| **주문 관리 (Order)** | CRUD + 검색. 주문상세(OrderDetail) 포함 — 결제 수단, 결제 금액 |
| **보상금 관리 (Reward)** | CRUD + 검색. 보상 사유 및 금액 관리 |
| **지급 관리 (Settle)** | 주문+보상 데이터 기반 지급 금액 자동 산출, 상태 관리 (대기→요청→승인→완료/반려) |
| **프론트엔드** | Vue 3 기반 SPA 어드민 UI (백엔드 API 연동) |
| **보안** | SQL 인젝션, XSS, CSRF 방어. 비밀번호 BCrypt 암호화 |
| **테스트** | 단위 테스트 (Service 계층) + 통합 테스트 (API 엔드포인트) |
| **문서화** | API 문서 (Swagger/OpenAPI), README |

#### Out-of-Scope (비구현 범위)

| 영역 | 제외 사유 |
|------|-----------|
| 실제 PG사 결제 연동 | 포트폴리오 범위 초과, 실제 금융 API 연동은 사업자 등록 필요 |
| Spring Batch 배치 처리 | 1인 개발 복잡도 관리. 서비스 레벨에서 일괄 처리 로직으로 대체 |
| 대규모 트래픽 처리 / 캐싱 | 비즈니스 로직 완성도에 집중 |
| 모바일 네이티브 앱 | 백엔드 포트폴리오 목적에 부합하지 않음 |
| CI/CD 파이프라인 | 선택적 확장 사항. Phase 3에서 GitHub Actions 도입 검토 |

### 1.4 성공 지표

| 지표 | 목표 | 측정 방법 |
|------|------|-----------|
| **기능 완성도** | 5대 도메인 CRUD + 검색 100% 구현 | 기능 체크리스트 기반 검수 |
| **테스트 커버리지** | Service 계층 80% 이상, 주요 API 통합 테스트 100% | JaCoCo 리포트 |
| **보안 검증** | SQL 인젝션, XSS, CSRF 방어 테스트 통과 | 수동 침투 테스트 + Spring Security Test |
| **코드 품질** | 메서드 평균 20줄 이하, 클래스 평균 200줄 이하 | 코드 리뷰 및 정적 분석 |
| **API 문서화** | 전체 엔드포인트 Swagger 문서화 100% | Swagger UI 접속 확인 |
| **프로젝트 관리** | GitHub Issue 기반 작업 추적률 100% | GitHub Project 보드 검토 |

### 1.5 제약 조건 및 전제 사항

| 구분 | 내용 |
|------|------|
| **개발 인원** | 1인 (백엔드 + Vue 프론트엔드) |
| **개발 기간** | 약 4~6주 (파일럿 프로젝트 기준) |
| **인증 방식** | 세션(Session) 기반으로 확정. JWT 사용하지 않음 |
| **프론트엔드** | Vue 3 (Composition API) + Vite. 백엔드와 별도 모듈 또는 멀티모듈 구성 |
| **데이터** | 실제 배달 주문 데이터가 아닌, 테스트용 시드 데이터로 개발 |
| **배포 환경** | 로컬 개발 환경 기준. 클라우드 배포는 선택적 확장 |

---

## 2. 레퍼런스 서비스 및 상세 근거

### 2.1 1차 레퍼런스 (직접 원본)

| # | 서비스명 | 출처 | 참고 근거 |
|---|----------|------|-----------|
| R-01 | **우아한형제들 정산시스템 파일럿 프로젝트** | [기술블로그 — 잊을만 하면 돌아오는 정산 신병들](https://techblog.woowahan.com/2711/) | 본 프로젝트의 **직접적인 요구사항 원본**. 5대 도메인(Member, Owner, Order, OrderDetail, Reward, Settle) 정의, 관리자/일반회원 권한 분리 기준, 엔티티 간 연관관계 명세 포함 |
| R-02 | **정산플랫폼팀 파일럿 회고 (2023)** | [기술블로그 — 신입 백엔드 개발자 혼돈의 파일럿 프로젝트 돌아보기](https://techblog.woowahan.com/7828/) | 동일 파일럿을 수행한 후속 신입 개발자 회고. Java, JPA, MySQL, Spring Boot 기술 스택 확인. 파일럿 수행 시 실제 어려움과 학습 포인트 사전 파악 |

### 2.2 2차 레퍼런스 (도메인 이해)

| # | 서비스명 | 출처 | 참고 근거 |
|---|----------|------|-----------|
| R-03 | **정산시스템팀 소개** | [기술블로그 — 정산시스템팀을 소개합니다](https://woowabros.github.io/woowabros/2020/11/20/team-settlement.html) | 정산 도메인의 비즈니스 전체 그림. 주문 데이터 → 정산금액 산출 → 사장님 지급의 End-to-End 플로우, 배민사장님광장을 통한 정산 정보 제공, 부가세 자료 생성 등 |
| R-04 | **정산시스템 운영 및 개발 문화** | [기술블로그 — 별로 안 궁금했지만 은근 궁금했던 개발 이야기](https://techblog.woowahan.com/6042/) | 정산 어드민 실제 운영 인사이트. "지급 요청"·"지급 승인" 운영 정책, 스테이징 환경 주의사항 등. Settle 상태 머신 설계의 핵심 참고 |
| R-05 | **배달의민족 사장님광장** | 배달의민족 앱 내 사장님 전용 서비스 | 업주 관점에서 정산 정보 제공 방식 이해. 정산내역 조회, 지급 예정일 확인 등 |

### 2.3 3차 레퍼런스 (기술 패턴)

| # | 서비스명 | 참고 근거 |
|---|----------|-----------|
| R-06 | **토스페이먼츠 정산 API 문서** | 매출 발생 → 정산 예정 → 정산 완료의 상태 관리 패턴, 결제 수단별 정산 주기 차이, 정산 금액 계산 공식 참고 |
| R-07 | **NHN커머스 정산 관리 시스템** | B2B 정산 어드민 UI/UX 패턴. 기간별 검색, 상태 필터링, 엑셀 다운로드, 페이징 등 |
| R-08 | **Spring Security 공식 문서 — Session Management** | 세션 기반 인증/인가 베스트 프랙티스. HttpSession 인증, 동시 세션 제어, 세션 고정 공격 방어, CSRF 토큰 연동 |
| R-09 | **Vue 3 공식 문서 — Composition API** | Vue 3 Composition API 패턴, Pinia 상태 관리, Vue Router SPA 라우팅 등 프론트엔드 아키텍처 참고 |

### 2.4 레퍼런스 활용 매트릭스

| 레퍼런스 | 요구사항 정의 | 도메인 설계 | 인증/인가 | 프론트엔드 | 보안 | 테스트 |
|----------|:---:|:---:|:---:|:---:|:---:|:---:|
| R-01 파일럿 요구사항 | ✅ | ✅ | | | | |
| R-02 파일럿 회고 | ✅ | ✅ | | | | ✅ |
| R-03 정산팀 소개 | | ✅ | | | | |
| R-04 운영/개발 문화 | | ✅ | ✅ | | | |
| R-05 사장님광장 | | ✅ | | ✅ | | |
| R-06 토스페이먼츠 | | ✅ | | | | |
| R-07 NHN커머스 | | | | ✅ | | |
| R-08 Spring Security | | | ✅ | | ✅ | ✅ |
| R-09 Vue 3 공식 문서 | | | | ✅ | | |

---

## 3. 핵심 기능 및 상세 스펙

### 3.1 시스템 권한 체계

| 도메인 | 기능 | 관리자 (ADMIN) | 일반 회원 (USER) | 비인증 사용자 |
|--------|------|:-:|:-:|:-:|
| **Member** | 회원가입 | ✅ | ✅ | ✅ |
| | 로그인 / 로그아웃 | ✅ | ✅ | ✅ (로그인만) |
| | 회원 목록 조회 | ✅ | ❌ | ❌ |
| | 권한 변경 | ✅ | ❌ | ❌ |
| | 본인 정보 수정 | ✅ | ✅ | ❌ |
| **Owner** | 생성 / 수정 / 삭제 | ✅ | ❌ | ❌ |
| | 검색 / 상세 조회 | ✅ | ✅ | ❌ |
| **Order** | 생성 / 수정 / 삭제 | ✅ | ❌ | ❌ |
| | 검색 / 상세 조회 | ✅ | ✅ | ❌ |
| **OrderDetail** | 생성 / 수정 / 삭제 | ✅ | ❌ | ❌ |
| | 조회 | ✅ | ✅ | ❌ |
| **Reward** | 생성 / 수정 / 삭제 | ✅ | ❌ | ❌ |
| | 검색 / 상세 조회 | ✅ | ✅ | ❌ |
| **Settle** | 생성 (지급 데이터 산출) | ✅ | ❌ | ❌ |
| | 상태 변경 (요청/승인/반려) | ✅ | ❌ | ❌ |
| | 검색 / 상세 조회 | ✅ | ✅ | ❌ |
| | 삭제 | ✅ | ❌ | ❌ |

### 3.2 인증/인가 (Authentication & Authorization)

#### 3.2.1 세션 기반 인증 플로우

```
[Vue SPA] --> POST /api/login (email, password)
    |
[Spring Security] --> UserDetailsService 조회 --> BCrypt 비밀번호 검증
    | (성공)
[서버] --> HttpSession 생성 --> SecurityContext에 인증 정보 저장
    |
[응답] --> Set-Cookie: JSESSIONID=abc123; HttpOnly; Secure; SameSite=Lax
    |
[이후 요청] --> Cookie: JSESSIONID=abc123 --> 세션 기반 인증 자동 처리
```

#### 3.2.2 인증 기능 상세

| 기능 | HTTP | 엔드포인트 | 설명 |
|------|------|-----------|------|
| 회원가입 | POST | `/api/members/signup` | 이메일, 비밀번호, 이름 입력. BCrypt 해싱 저장 |
| 로그인 | POST | `/api/login` | Spring Security 폼 로그인. 세션 생성 및 JSESSIONID 쿠키 발급 |
| 로그아웃 | POST | `/api/logout` | 세션 무효화 및 쿠키 삭제 |
| 현재 사용자 정보 | GET | `/api/members/me` | 세션 기반 인증된 사용자 정보 반환 (Vue 상태 동기화용) |

#### 3.2.3 세션 관리 정책

| 정책 | 설정값 | 근거 |
|------|--------|------|
| 세션 타임아웃 | 30분 | 보안과 사용성 균형 |
| 동시 세션 제한 | 1개 (최후 로그인 우선) | 계정 공유 방지 |
| 세션 고정 공격 방어 | `changeSessionId` | 로그인 시 세션 ID 변경 |
| 쿠키 설정 | `HttpOnly`, `SameSite=Lax` | XSS 세션 탈취 방지 |

### 3.3 어드민 회원 관리 (Member)

정산 어드민에 접근하는 내부 직원(정산팀, CS팀 등)을 의미합니다.

#### 기능 목록

| 기능 | HTTP | 엔드포인트 | 권한 |
|------|------|-----------|------|
| 회원가입 | POST | `/api/members/signup` | 누구나 |
| 로그인 | POST | `/api/login` | 누구나 |
| 로그아웃 | POST | `/api/logout` | 인증된 사용자 |
| 본인 정보 조회 | GET | `/api/members/me` | 인증된 사용자 |
| 본인 정보 수정 | PUT | `/api/members/me` | 인증된 사용자 |
| 회원 목록 조회 | GET | `/api/admin/members` | 관리자 |
| 회원 상세 조회 | GET | `/api/admin/members/{id}` | 관리자 |
| 권한 변경 | PATCH | `/api/admin/members/{id}/role` | 관리자 |
| 회원 삭제 | DELETE | `/api/admin/members/{id}` | 관리자 |

#### 입력 필드 정의

| 필드명 | 타입 | 필수 | 유효성 규칙 | 설명 |
|--------|------|:---:|-------------|------|
| email | String | ✅ | 이메일 형식, 최대 100자, 중복 불가 | 로그인 ID |
| password | String | ✅ | 최소 8자, 영문+숫자+특수문자 조합 | BCrypt 해싱 저장 |
| name | String | ✅ | 최소 2자, 최대 20자 | 표시 이름 |
| role | Enum | — | ADMIN / USER (기본값: USER) | 관리자만 변경 가능 |

#### 비즈니스 규칙

- 회원가입 시 역할은 무조건 `USER`. 관리자 역할은 기존 관리자만 부여 가능
- 이메일 시스템 전체 고유 (Unique Constraint)
- 비밀번호 BCrypt(strength=10) 단방향 해싱. 평문 저장 절대 금지
- 시스템에 최소 1명의 관리자 존재 보장
- 삭제는 논리 삭제(soft delete)

### 3.4 B2B 업주 관리 (Owner)

배달의민족 가맹점 업주. 업주는 여러 주문(1:N), 보상금(1:N), 지급 내역(1:N)을 가집니다.

#### 기능 목록

| 기능 | HTTP | 엔드포인트 | 권한 |
|------|------|-----------|------|
| 업주 등록 | POST | `/api/owners` | 관리자 |
| 업주 목록 검색 | GET | `/api/owners` | 관리자/일반회원 |
| 업주 상세 조회 | GET | `/api/owners/{id}` | 관리자/일반회원 |
| 업주 정보 수정 | PUT | `/api/owners/{id}` | 관리자 |
| 업주 삭제 | DELETE | `/api/owners/{id}` | 관리자 |
| 업주별 주문 조회 | GET | `/api/owners/{id}/orders` | 관리자/일반회원 |
| 업주별 지급 조회 | GET | `/api/owners/{id}/settles` | 관리자/일반회원 |

#### 입력 필드 정의

| 필드명 | 타입 | 필수 | 유효성 규칙 | 설명 |
|--------|------|:---:|-------------|------|
| businessName | String | ✅ | 최소 1자, 최대 100자 | 상호명 |
| businessNumber | String | ✅ | 사업자번호 형식 (###-##-#####), 중복 불가 | 사업자등록번호 |
| ownerName | String | ✅ | 최소 2자, 최대 20자 | 대표자명 |
| phone | String | ✅ | 전화번호 형식 검증 | 연락처 |
| email | String | — | 이메일 형식 | 업주 이메일 |
| address | String | — | 최대 200자 | 사업장 주소 |
| bankName | String | — | — | 정산 입금 은행명 |
| accountNumber | String | — | — | 정산 입금 계좌번호 |

#### 비즈니스 규칙

- 사업자등록번호 시스템 전체 고유 (Unique Constraint)
- 주문이 존재하는 업주는 물리 삭제 불가, 논리 삭제만 허용
- 상세 조회 시 총 주문 건수, 총 주문 금액, 총 보상 금액, 최근 지급 상태 요약 반환

### 3.5 주문 관리 (Order / OrderDetail)

#### Order (주문)

| 기능 | HTTP | 엔드포인트 | 권한 |
|------|------|-----------|------|
| 주문 등록 | POST | `/api/orders` | 관리자 |
| 주문 목록 검색 | GET | `/api/orders` | 관리자/일반회원 |
| 주문 상세 조회 | GET | `/api/orders/{id}` | 관리자/일반회원 |
| 주문 수정 | PUT | `/api/orders/{id}` | 관리자 |
| 주문 삭제 | DELETE | `/api/orders/{id}` | 관리자 |

**입력 필드:**

| 필드명 | 타입 | 필수 | 유효성 규칙 | 설명 |
|--------|------|:---:|-------------|------|
| ownerId | Long | ✅ | 존재하는 Owner ID | 업주 FK |
| orderDateTime | LocalDateTime | ✅ | 미래 일시 불가 | 주문 일시 |
| status | Enum | ✅ | RECEIVED / COMPLETED / CANCELLED | 주문 상태 |
| totalAmount | BigDecimal | ✅ | 0 이상 | 총 주문 금액 |

#### OrderDetail (주문상세)

| 기능 | HTTP | 엔드포인트 | 권한 |
|------|------|-----------|------|
| 주문상세 등록 | POST | `/api/orders/{orderId}/details` | 관리자 |
| 주문상세 조회 | GET | `/api/orders/{orderId}/details` | 관리자/일반회원 |
| 주문상세 수정 | PUT | `/api/orders/{orderId}/details/{id}` | 관리자 |
| 주문상세 삭제 | DELETE | `/api/orders/{orderId}/details/{id}` | 관리자 |

**입력 필드:**

| 필드명 | 타입 | 필수 | 유효성 규칙 | 설명 |
|--------|------|:---:|-------------|------|
| orderId | Long | ✅ | 존재하는 Order ID | 주문 FK |
| paymentMethod | Enum | ✅ | CARD / CASH / BAEMIN_PAY / BANK_TRANSFER | 결제 수단 |
| paymentAmount | BigDecimal | ✅ | 0 이상 | 결제 금액 |
| feeAmount | BigDecimal | ✅ | 0 이상, paymentAmount 이하 | 수수료 |

#### 비즈니스 규칙

- 주문 등록 시 최소 1개 OrderDetail 필수
- OrderDetail `paymentAmount` 합계 = Order `totalAmount` (정합성 검증)
- `COMPLETED` 주문만 정산 대상
- 금액 필드는 반드시 `BigDecimal` 사용

### 3.6 보상 금액 관리 (Reward)

#### 기능 목록

| 기능 | HTTP | 엔드포인트 | 권한 |
|------|------|-----------|------|
| 보상 등록 | POST | `/api/rewards` | 관리자 |
| 보상 목록 검색 | GET | `/api/rewards` | 관리자/일반회원 |
| 보상 상세 조회 | GET | `/api/rewards/{id}` | 관리자/일반회원 |
| 보상 수정 | PUT | `/api/rewards/{id}` | 관리자 |
| 보상 삭제 | DELETE | `/api/rewards/{id}` | 관리자 |

**입력 필드:**

| 필드명 | 타입 | 필수 | 유효성 규칙 | 설명 |
|--------|------|:---:|-------------|------|
| ownerId | Long | ✅ | 존재하는 Owner ID | 업주 FK |
| rewardAmount | BigDecimal | ✅ | 0 초과 | 보상 금액 |
| reason | Enum | ✅ | DELIVERY_ACCIDENT / SYSTEM_ERROR / PROMOTION / ETC | 보상 사유 |
| reasonDetail | String | ✅ | 최소 10자, 최대 500자 | 사유 상세 |
| rewardDateTime | LocalDateTime | ✅ | 미래 일시 불가 | 보상 발생 일시 |

#### 비즈니스 규칙

- 보상 금액 양수만 허용
- 이미 정산(Settle)에 포함된 보상금은 수정/삭제 불가 (settled 플래그로 관리)
- 다음 정산 시 자동 합산

### 3.7 지급 관리 (Settle)

**페이라인의 핵심 도메인**입니다.

#### 기능 목록

| 기능 | HTTP | 엔드포인트 | 권한 |
|------|------|-----------|------|
| 지급 데이터 생성 | POST | `/api/settles` | 관리자 |
| 지급 목록 검색 | GET | `/api/settles` | 관리자/일반회원 |
| 지급 상세 조회 | GET | `/api/settles/{id}` | 관리자/일반회원 |
| 지급 요청 | PATCH | `/api/settles/{id}/request` | 관리자 |
| 지급 승인 | PATCH | `/api/settles/{id}/approve` | 관리자 |
| 지급 완료 | PATCH | `/api/settles/{id}/complete` | 관리자 |
| 지급 반려 | PATCH | `/api/settles/{id}/reject` | 관리자 |
| 지급 삭제 | DELETE | `/api/settles/{id}` | 관리자 |

#### 지급 금액 산출 공식

```
totalOrderAmount  = 해당 기간 COMPLETED 주문의 OrderDetail.paymentAmount 합계
totalFeeAmount    = 해당 기간 COMPLETED 주문의 OrderDetail.feeAmount 합계
totalRewardAmount = 해당 기간 Reward.rewardAmount 합계

settlementAmount  = (totalOrderAmount - totalFeeAmount) + totalRewardAmount
```

#### 지급 상태 머신

```
 [PENDING] ---요청---> [REQUESTED] ---승인---> [APPROVED] ---완료---> [COMPLETED]
   (대기)                 (요청)   ---반려--->  [REJECTED]   (승인)       (완료)
                                                (반려)
```

| 현재 상태 | 허용 전이 | 조건 |
|-----------|----------|------|
| PENDING | REQUESTED | 관리자가 지급 요청 |
| REQUESTED | APPROVED | 관리자가 승인 |
| REQUESTED | REJECTED | 반려 사유 입력 필수 |
| APPROVED | COMPLETED | 실제 지급 완료 확인 |
| PENDING | 삭제 가능 | 대기 상태에서만 삭제 허용 |

#### 입력 필드

| 필드명 | 타입 | 필수 | 설명 |
|--------|------|:---:|------|
| ownerId | Long | ✅ | 정산 대상 업주 |
| settleStartDate | LocalDate | ✅ | 정산 시작일 |
| settleEndDate | LocalDate | ✅ | 정산 종료일 |
| totalOrderAmount | BigDecimal | 자동산출 | 총 주문 금액 |
| totalFeeAmount | BigDecimal | 자동산출 | 총 수수료 |
| totalRewardAmount | BigDecimal | 자동산출 | 총 보상 금액 |
| settlementAmount | BigDecimal | 자동산출 | 최종 지급 금액 |
| status | Enum | 자동관리 | PENDING / REQUESTED / APPROVED / COMPLETED / REJECTED |
| rejectionReason | String | 반려 시 필수 | 반려 사유 |

#### 비즈니스 규칙

- 동일 업주·동일 기간 중복 정산 방지
- 금액은 시스템 자동 산출 (수동 입력 불가)
- 상태 전이는 정해진 순서만 허용
- REQUESTED 이후 삭제 불가
- **모든 금액 연산은 BigDecimal** (부동소수점 오차 절대 불허)

### 3.8 공통 기능 요구사항

#### 검색 및 페이징

| 항목 | 사양 |
|------|------|
| **페이징** | `page` (0-based), `size` (기본 20, 최대 100) |
| **정렬** | `sort` 파라미터. 예: `sort=createdAt,desc` |
| **기간 검색** | `startDate`, `endDate` (ISO 8601) |
| **키워드 검색** | `keyword` 통합 검색 |
| **상태 필터** | `status` 파라미터 |

#### 공통 응답 포맷

```json
// 성공
{ "success": true, "data": { }, "message": null }

// 페이징
{ "success": true, "data": { "content": [], "page": 0, "size": 20, "totalElements": 150, "totalPages": 8 } }

// 에러
{ "success": false, "data": null, "message": "존재하지 않는 업주입니다.", "errorCode": "OWNER_NOT_FOUND" }
```

#### Auditing 필드

| 필드 | 타입 | 설명 |
|------|------|------|
| createdAt | LocalDateTime | 생성 일시 (자동) |
| updatedAt | LocalDateTime | 수정 일시 (자동) |
| createdBy | String | 생성자 (세션에서 추출) |
| updatedBy | String | 수정자 (세션에서 추출) |

### 3.9 API 엔드포인트 설계 요약

| 도메인 | Prefix | 주요 메서드 | 비고 |
|--------|--------|------------|------|
| 인증 | `/api/login`, `/api/logout` | POST | Spring Security 처리 |
| Member | `/api/members`, `/api/admin/members` | GET, POST, PUT, PATCH, DELETE | 일반/관리자 분리 |
| Owner | `/api/owners` | GET, POST, PUT, DELETE | 하위: `/orders`, `/settles` |
| Order | `/api/orders` | GET, POST, PUT, DELETE | 하위: `/details` |
| Reward | `/api/rewards` | GET, POST, PUT, DELETE | — |
| Settle | `/api/settles` | GET, POST, PATCH, DELETE | 상태 전이 API |

### 3.10 비기능 요구사항 (NFR)

| 구분 | 요구사항 | 목표 |
|------|----------|------|
| **응답 시간** | 일반 API | 500ms 이내 |
| **응답 시간** | 검색/페이징 API | 1,000ms 이내 |
| **데이터 무결성** | 금액 계산 정합성 | 오차 0원 |
| **보안** | OWASP Top 10 | SQL 인젝션, XSS, CSRF 방어 |
| **호환성** | 브라우저 | Chrome, Edge, Firefox 최신 2개 버전 |
| **코드 품질** | 테스트 커버리지 | Service 계층 80% 이상 |

---

## 4. 추가 제안 기능

### 4.1 Phase 2 — 포트폴리오 차별화 기능

| # | 기능 | 설명 | 기술적 포인트 | 예상 소요 |
|---|------|------|--------------|-----------|
| P2-01 | **대시보드** | 정산 현황 요약 카드 + 추이 차트 | 집계 쿼리, Chart.js/ECharts + Vue 컴포넌트 | 2~3일 |
| P2-02 | **글로벌 예외 처리** | `@RestControllerAdvice` 통합 예외 핸들러, 커스텀 예외 체계 | 예외 계층 구조, HTTP 상태 코드 매핑 | 1일 |
| P2-03 | **Swagger API 문서화** | SpringDoc OpenAPI 전체 API 자동 문서화 | API 설계 역량, 협업 도구 활용 | 1일 |
| P2-04 | **엑셀 다운로드** | 정산/주문 내역 Excel 다운로드 | Apache POI, 스트리밍 다운로드 | 1~2일 |
| P2-05 | **감사 로그** | 데이터 변경 이력 자동 기록 및 조회 | JPA Auditing, AOP | 2일 |

### 4.2 Phase 3 — 고급 확장 기능

| # | 기능 | 설명 | 기술적 포인트 | 예상 소요 |
|---|------|------|--------------|-----------|
| P3-01 | **정산 통계 API** | 기간별/업주별/결제수단별 통계 | MyBatis 복잡 쿼리, GROUP BY 최적화 | 2~3일 |
| P3-02 | **일괄 정산 생성** | 전체 업주 대상 일괄 생성 | Virtual Threads 활용 대량 처리, 트랜잭션 관리 | 2~3일 |
| P3-03 | **이메일 알림 (모의)** | 지급 상태 변경 시 이벤트 발행 | Spring Events, 비동기 처리 | 1~2일 |
| P3-04 | **CI/CD 파이프라인** | GitHub Actions 자동 빌드/테스트 | DevOps 역량 | 1~2일 |
| P3-05 | **Docker 컨테이너화** | Dockerfile + Docker Compose | 환경 일관성 보장 | 1일 |

### 4.3 기능 로드맵

```
Week 1~2 : Phase 1 -- 핵심 기능 (Member, Owner, Order, Reward, Settle + 인증/인가)
Week 3~4 : Phase 1 마무리 + 테스트 작성 + Vue 프론트엔드 연동
Week 5   : Phase 2 -- 대시보드, 예외 처리, Swagger, 엑셀 다운로드
Week 6   : Phase 3 (선택) + 리팩토링 + README + 포트폴리오 정리
```

---

## 5. 사용자 페르소나 및 시나리오

### 5.1 페르소나 정의

#### 페르소나 A: 정산팀 매니저 (관리자)

| 항목 | 내용 |
|------|------|
| **이름** | 김정산 |
| **나이 / 직책** | 32세 / 정산팀 매니저 (5년차) |
| **시스템 역할** | 관리자 (ADMIN) |
| **주요 업무** | 일별 정산 데이터 생성·검증, 지급 승인/반려, 보상금 등록, 신규 업주 등록 |
| **Pain Point** | ① 수작업 검증의 시간 소모와 실수 가능성 ② 지급 승인 프로세스 추적 어려움 ③ 보상금 중복 등록 확인 부담 |
| **기대 사항** | ① 금액 자동 산출 ② 상태 기반 체계적 관리 ③ 빠른 검색 |

#### 페르소나 B: CS팀 상담원 (일반 회원)

| 항목 | 내용 |
|------|------|
| **이름** | 이서비스 |
| **나이 / 직책** | 27세 / CS팀 상담원 (2년차) |
| **시스템 역할** | 일반 회원 (USER) |
| **주요 업무** | 업주 문의 응대 시 정산·주문·지급 내역 조회 |
| **Pain Point** | ① 정보 검색 속도 부족 ② 유사 상호명 구별 어려움 ③ 지급 상태 직관성 부족 |
| **기대 사항** | ① 다양한 조건 빠른 검색 ② 업주별 통합 조회 ③ 상태 시각적 구분 |

#### 페르소나 C: 가맹점 업주 (간접 사용자)

| 항목 | 내용 |
|------|------|
| **이름** | 박사장 |
| **나이 / 직책** | 45세 / "행복치킨" 사장님 |
| **시스템 역할** | 직접 접근 불가 (CS 통한 간접 사용자) |
| **기대 사항** | CS팀이 빠르고 정확하게 정산 현황 안내 |

### 5.2 핵심 사용자 여정 (User Journey)

#### 관리자 일일 정산 워크플로우

```
[09:00] 로그인 --> 대시보드 확인
[09:20] 주문 관리 --> 전일 날짜 검색 --> 완료 건수/금액 확인
[09:30] 보상금 관리 --> 전일 보상 건 확인
[09:40] 지급 관리 --> "지급 데이터 생성" --> 자동 산출
[10:00] 금액 검증 --> "지급 요청"
[10:30] 팀장 승인 --> "지급 완료" 처리
```

#### CS 직원 문의 대응

```
[문의 수신] "정산금이 안 들어왔어요"
[어드민] 업주 검색 --> 지급 내역 확인 --> 상태: "지급 요청"
[안내] "승인 대기 중, 영업일 1~2일 내 입금 예정"
```

### 5.3 상세 사용 시나리오

#### 시나리오 1: 신규 업주 등록 및 첫 정산

> 관리자가 "맛있는분식" 업주를 등록합니다. 일주일간 주문 데이터가 쌓인 후, 지급 관리에서 해당 기간을 지정하여 지급 데이터를 생성합니다. 시스템이 총 주문 금액 850,000원, 수수료 25,500원을 자동 집계하여 최종 지급 금액 824,500원을 산출합니다. 검증 후 "지급 요청" → "지급 승인" → "지급 완료"를 순차 처리합니다.

#### 시나리오 2: 배송 사고 보상금 등록

> "행복치킨" 업주님의 배송 사고에 대해 보상 사유 "배송 중 음식 파손", 금액 15,000원을 등록합니다. 다음 정산 시 해당 보상금이 자동으로 지급 금액에 합산됩니다.

#### 시나리오 3: 지급 반려 및 재처리

> 지급 요청 건의 금액 이상 발견 시, 반려 사유를 입력하여 반려 처리합니다. 문제 주문 수정 후 지급 데이터를 재생성합니다.

#### 시나리오 4: CS 직원의 정산 문의 대응

> 업주님의 "정산금이 적다" 문의에 대해, 업주 상세 페이지에서 최근 2건의 지급 내역을 비교하여 주문 건수 감소가 원인임을 확인하고 안내합니다.

---

## 6. 기술 스택 권장사항

### 6.1 기술 스택 전체 구성도

```
+-------------------------------------------------------------+
|                    프론트엔드 (Client)                        |
|  +-------------------------------------------------------+  |
|  |  Vue 3 (Composition API)  |  Pinia  |  Vue Router     |  |
|  |  Vite  |  Axios  |  ECharts / Chart.js                |  |
|  +-------------------------------------------------------+  |
+-------------------------------------------------------------+
|                       백엔드 (Server)                        |
|  +-------------------------------------------------------+  |
|  |  Java 21  |  Spring Boot 4.0.2  |  Spring Security 7.0|  |
|  |  세션 기반 인증  |  RBAC 권한 제어  |  Virtual Threads  |  |
|  +-------------------------------------------------------+  |
|  |  Service Layer  |  OOP + 클린 코드  |  Bean Validation  |  |
|  +-------------------------------------------------------+  |
|  |  JPA (Hibernate 7.2) -- 엔티티 CRUD                    |  |
|  |  MyBatis 3.5.x -- 복잡한 검색/집계 쿼리                 |  |
|  +-------------------------------------------------------+  |
|  |  JUnit Jupiter 6.0.2 + Mockito 5.20 (단위)                      |  |
|  |  Spring Boot Test + MockMvc (통합)                     |  |
|  +-------------------------------------------------------+  |
+-------------------------------------------------------------+
|                      데이터베이스 (DB)                        |
|  +---------------------+  +---------------------------+     |
|  |  MySQL 9.x (운영)    |  |  H2 Database (개발/테스트) |     |
|  +---------------------+  +---------------------------+     |
+-------------------------------------------------------------+
|                      개발 도구 (Tooling)                      |
|  Gradle (Groovy)  |  Lombok  |  Git & GitHub                |
|  GitHub Project & Issue  |  SpringDoc OpenAPI 2.8+          |
+-------------------------------------------------------------+
```

### 6.2 핵심 기술 스택 상세

| 구분 | 기술 | 버전 | 선정 근거 |
|------|------|------|-----------|
| **언어** | Java | 21 (LTS) | 최신 LTS. Virtual Threads(가상 스레드), Record, Pattern Matching, Sealed Class, Switch Expression 등 모던 Java 기능 전면 활용 |
| **프레임워크** | Spring Boot | 4.0.2 | 2026년 2월 기준 최신 GA. Spring Framework 7.0.3 기반, Java 21 최적화, Spring Framework 7.0 기반, Jakarta EE 11, 모듈화 아키텍처, Virtual Threads 안정 지원 |
| **보안** | Spring Security | 6.5.x | Spring Boot 4.0.2 관리 의존성. 세션 관리, CSRF, OAuth 2.1, 강화된 비밀번호 인코더 기본 적용 |
| **ORM** | JPA (Hibernate) | 7.2.x | Spring Boot 4.0.2 관리 의존성. 엔티티 매핑, 연관관계 관리, JPA Auditing |
| **SQL 매퍼** | MyBatis | 3.5.x | mybatis-spring-boot-starter 4.x. 동적 쿼리, 복잡한 JOIN, 집계 쿼리에 최적 |
| **프론트엔드** | Vue | 3.x (Composition API) | 학습 곡선이 낮고 SPA 구축에 적합. Composition API로 TypeScript 친화적 코드 작성 가능 |
| **프론트 빌드** | Vite | 6.x | Vue 공식 권장 빌드 도구. HMR(Hot Module Replacement) 초고속, ES Modules 기반 |
| **프론트 상태 관리** | Pinia | 3.x | Vue 3 공식 상태 관리 라이브러리. Vuex 후속으로 Composition API와 완벽 호환 |
| **프론트 라우팅** | Vue Router | 4.x | Vue 3 공식 라우터. History 모드 SPA 라우팅 |
| **HTTP 클라이언트** | Axios | 1.x | Fetch API 대비 인터셉터, 요청 취소, CSRF 토큰 자동 첨부 등 편의 기능 우수 |
| **빌드** | Gradle | Groovy DSL (9.x / 최소 8.14) | 파일럿 프로젝트 요구사항에 명시 |
| **유틸** | Lombok | 최신 | 보일러플레이트 제거 |
| **DB (개발)** | H2 | 인메모리 | 별도 설치 없이 즉시 구동 |
| **DB (운영)** | MySQL | 9.x | Spring Boot 4.0.2 관리 의존성(mysql-connector-j 9.2). UTF-8MB4 |
| **API 문서** | SpringDoc OpenAPI | 3.x (Boot 4 호환) | Spring Boot 4.0.2 호환. Swagger UI 자동 생성 |
| **테스트** | JUnit Jupiter | 6.0.2 | Spring Boot 4.0.2 관리 의존성 |
| **테스트 모킹** | Mockito | 5.20 | Spring Boot 4.0.2 관리 의존성 |
| **버전 관리** | Git & GitHub | — | GitHub Project + Issue + PR |

### 6.3 세션 기반 인증/인가 아키텍처

#### JWT 대신 세션을 선택한 근거

| 비교 기준 | 세션 (Session) | JWT |
|-----------|---------------|-----|
| **프로젝트 적합성** | ✅ 단일 서버 어드민 시스템에 자연스러움 | 마이크로서비스/멀티 도메인에 적합 |
| **서버 측 제어** | ✅ 즉시 로그아웃, 동시 접속 제어 용이 | 만료 전 서버 측 무효화 어려움 |
| **보안** | ✅ 세션 ID만 쿠키 저장 (HttpOnly) | 페이로드에 사용자 정보 포함 |
| **Spring Security 친화** | ✅ 기본 메커니즘, 추가 설정 최소 | 커스텀 필터 직접 구현 필요 |
| **CSRF 방어** | ✅ CSRF 토큰과 자연스럽게 연동 | 별도 관리 필요 |

#### Spring Security 설정 핵심

| 설정 항목 | 내용 |
|-----------|------|
| **인증** | `formLogin()` 커스텀 처리. JSON 응답 반환 핸들러 |
| **인가** | `/api/admin/**` → ADMIN만. 메서드 레벨 `@PreAuthorize` 병행 |
| **세션 정책** | `IF_REQUIRED` — 인증 성공 시에만 생성 |
| **동시 세션** | `maximumSessions(1)` — 후속 로그인 시 기존 만료 |
| **CSRF** | 활성화. Vue의 Axios 인터셉터에서 CSRF 토큰 자동 첨부 |
| **로그아웃** | 세션 무효화 + 쿠키 삭제 + SecurityContext 클리어 |

### 6.4 Vue 3 프론트엔드 전략

#### Vue 3를 선택한 근거

| 비교 기준 | Vue 3 | React | Vanilla JS |
|-----------|-------|-------|------------|
| **학습 곡선** | ✅ 낮음. 템플릿 문법 직관적 | 중간. JSX 학습 필요 | 낮지만 구조화 어려움 |
| **SPA 생태계** | ✅ Vue Router, Pinia 공식 지원 | React Router, Redux 등 선택 피로 | 직접 구현 필요 |
| **1인 개발 생산성** | ✅ SFC(Single File Component)로 빠른 UI 개발 | 생산성 높지만 보일러플레이트 다소 많음 | 컴포넌트 재사용 어려움 |
| **어드민 UI 적합성** | ✅ 테이블, 폼, 모달 등 반복 UI 패턴에 컴포넌트 재활용 최적 | 동일 | 코드 중복 발생 |
| **백엔드 개발자 친화** | ✅ HTML 기반 템플릿으로 백엔드 개발자가 접근하기 쉬움 | JSX는 JavaScript 중심 | — |

#### Vue 프로젝트 구조

```
frontend/
+-- index.html
+-- vite.config.js
+-- package.json
+-- src/
    +-- main.js                 # 앱 진입점
    +-- App.vue                 # 루트 컴포넌트
    +-- router/
    |   +-- index.js            # Vue Router 설정 (라우트 가드 포함)
    +-- stores/
    |   +-- auth.js             # Pinia — 인증 상태 관리
    |   +-- owner.js            # Pinia — 업주 상태
    |   +-- settle.js           # Pinia — 지급 상태
    +-- api/
    |   +-- client.js           # Axios 인스턴스 (CSRF 토큰 인터셉터, 에러 핸들링)
    |   +-- authApi.js
    |   +-- ownerApi.js
    |   +-- orderApi.js
    |   +-- rewardApi.js
    |   +-- settleApi.js
    +-- views/
    |   +-- LoginView.vue
    |   +-- DashboardView.vue
    |   +-- OwnerListView.vue
    |   +-- OwnerDetailView.vue
    |   +-- OrderListView.vue
    |   +-- RewardListView.vue
    |   +-- SettleListView.vue
    |   +-- SettleDetailView.vue
    +-- components/
    |   +-- common/
    |   |   +-- AppTable.vue    # 공통 테이블
    |   |   +-- AppPagination.vue
    |   |   +-- AppModal.vue
    |   |   +-- AppSearchBar.vue
    |   |   +-- StatusBadge.vue # 지급 상태 배지
    |   +-- layout/
    |       +-- AppHeader.vue
    |       +-- AppSidebar.vue
    |       +-- AppLayout.vue
    +-- composables/
    |   +-- useSearch.js        # 검색/페이징 공통 로직
    |   +-- useAuth.js          # 인증 상태 확인
    +-- assets/
        +-- styles/
            +-- variables.css
            +-- global.css
```

#### Vue + Spring 세션 통합 패턴

```
[Vite Dev Server :5173] --(proxy)--> [Spring Boot :8080]  (개발 환경)
[Nginx / Spring Static] <-- Vue 빌드 결과물 서빙           (운영 환경)

CSRF 처리:
1. Spring Security가 CookieCsrfTokenRepository로 XSRF-TOKEN 쿠키 발급
2. Axios 인터셉터가 쿠키에서 토큰 읽어 X-XSRF-TOKEN 헤더에 자동 첨부
3. Spring Security가 헤더의 토큰 검증
```

### 6.5 JPA + MyBatis 병행 전략

| 용도 | 기술 | 적용 |
|------|------|------|
| **엔티티 CRUD** | JPA (Spring Data JPA) | `JpaRepository` 자동 생성 |
| **연관관계·감사 필드** | JPA | `@OneToMany`, `@CreatedDate` 등 |
| **복잡한 검색** | MyBatis | 다중 조건 동적 쿼리 (XML 기반) |
| **지급 금액 산출** | MyBatis | `SUM`, `GROUP BY`, 다중 `JOIN` 집계 |
| **통계/리포트** | MyBatis | 기간별/업주별/결제수단별 통계 |

### 6.6 보안 요구사항

| 위협 | 방어 기법 | 구현 방안 |
|------|-----------|-----------|
| **SQL 인젝션** | Parameterized Query | JPA Prepared Statement. MyBatis `#{}` 필수 (`${}` 금지) |
| **XSS** | 입출력 이스케이핑 | CSP 헤더. Vue의 자동 이스케이핑 (`v-html` 지양) |
| **CSRF** | CSRF 토큰 | Spring Security + `CookieCsrfTokenRepository`. Axios 인터셉터 자동 첨부 |
| **세션 탈취** | 쿠키 보안 | `HttpOnly`, `SameSite=Lax`, HTTPS 시 `Secure` |
| **세션 고정** | ID 재생성 | `changeSessionId()` |
| **비밀번호 유출** | 단방향 해싱 | BCrypt(strength=10) |
| **권한 우회** | 다층 검증 | URL 패턴 + `@PreAuthorize` 이중 검증 |
| **입력값 조작** | 서버 검증 | Bean Validation + 커스텀 Validator |
| **에러 정보 노출** | 최소화 | 프로덕션 스택 트레이스 비노출 |

### 6.7 테스트 전략

#### 테스트 피라미드

```
              / E2E (Cypress/Playwright) \        <-- 선택적
             /--------------------------\
            /   통합 테스트 (MockMvc)      \       <-- API 엔드포인트
           /------------------------------\
          /      단위 테스트 (JUnit 5)       \     <-- Service 비즈니스 로직
         /----------------------------------\
```

#### 핵심 테스트 시나리오

| 도메인 | 시나리오 | 유형 |
|--------|---------|------|
| **Settle** | 지급 금액 산출 정확성 | 단위 |
| **Settle** | 상태 전이 규칙 위반 시 예외 | 단위 |
| **Settle** | 동일 업주/기간 중복 방지 | 단위 |
| **Order** | OrderDetail 합계 = totalAmount | 단위 |
| **Auth** | 비인증 접근 → 401 | 통합 |
| **Auth** | 일반회원 관리자 API → 403 | 통합 |
| **Auth** | CSRF 토큰 없이 POST → 403 | 보안 |

### 6.8 개발 원칙 및 컨벤션

#### 코드 품질 원칙

| 원칙 | 적용 |
|------|------|
| **OOP** | Rich Domain Model. 캡슐화. 조합 우선. Enum 상태 관리. Java 21 Record로 DTO 정의 |
| **클린 코드** | 의미 있는 네이밍. SRP. 메서드 20줄 이내. Early Return. 주석 최소화 |
| **계층형 아키텍처** | Controller → Service → Repository. DTO ↔ Entity 변환. `@Transactional` 경계 |
| **Java 21 활용** | `record`로 DTO/VO 정의, `sealed interface`로 예외 계층, Pattern Matching, Virtual Threads (Phase 3) |

#### 패키지 구조

```
src/main/java/com/payline/
+-- PayLineApplication.java
+-- global/
|   +-- config/          # SecurityConfig, WebConfig, MyBatisConfig
|   +-- common/          # ApiResponse, BaseEntity, 페이징 DTO
|   +-- error/           # 글로벌 예외 핸들러, 커스텀 예외, ErrorCode
|   +-- auth/            # UserDetailsService, 핸들러
+-- member/              # controller / service / repository / domain / dto
+-- owner/
+-- order/
+-- reward/
+-- settle/
```

#### 커밋 메시지 컨벤션

| 접두사 | 용도 | 예시 |
|--------|------|------|
| `feat:` | 새 기능 | `feat: 지급 데이터 생성 API 구현` |
| `fix:` | 버그 수정 | `fix: BigDecimal 연산 오류 수정` |
| `refactor:` | 리팩토링 | `refactor: SettleService 메서드 분리` |
| `test:` | 테스트 | `test: 지급 상태 전이 단위 테스트 추가` |
| `docs:` | 문서 | `docs: README 추가` |
| `chore:` | 설정 | `chore: Spring Security 의존성 추가` |

### 6.9 프로젝트 관리 전략

#### 개발 일정 (6주)

| 주차 | Phase | 주요 작업 | 산출물 |
|------|-------|-----------|--------|
| **1주차** | Phase 1 | 프로젝트 설정 (Gradle, DB, Security). Member 인증/인가 | 세션 인증 동작, Member API |
| **2주차** | Phase 1 | Owner, Order, OrderDetail 구현 | Owner/Order CRUD + 검색 API |
| **3주차** | Phase 1 | Reward, Settle 구현. 금액 산출 로직. 상태 머신 | 5대 도메인 API 완성 |
| **4주차** | Phase 1 | Vue 3 프론트엔드 구현. API 연동. 단위 테스트 | 프론트 연동, Service 테스트 |
| **5주차** | Phase 2 | 통합/보안 테스트. 대시보드, Swagger, 예외 처리 | 커버리지 80%+, Phase 2 |
| **6주차** | 마무리 | 리팩토링, README, 포트폴리오 정리 | 최종 산출물 |

---

## 용어 사전

| 용어 | 영문 | 정의 |
|------|------|------|
| 정산 | Settlement | 주문 데이터를 집계하여 업주 지급 금액을 산출하는 프로세스 |
| 어드민 | Admin | 정산시스템 관리 웹 도구 |
| 업주 | Owner | 배달의민족 가맹점 사업자 |
| 주문 | Order | 소비자가 발생시킨 개별 거래 건 |
| 주문상세 | OrderDetail | 주문의 결제 수단별 금액 내역 |
| 보상금 | Reward | 특정 사유로 업주에게 보상하는 금액 |
| 지급 | Settle | 특정 기간의 주문/보상 집계 결과인 최종 지급 금액 관리 단위 |
| 수수료 | Fee | 플랫폼이 차감하는 서비스 이용 수수료 |
| RBAC | Role-Based Access Control | 역할 기반 접근 제어 |
| Soft Delete | 논리 삭제 | 물리 삭제 대신 삭제 플래그 설정 |
| Virtual Threads | 가상 스레드 | Java 21의 경량 스레드. I/O 바운드 작업 동시성 극대화 |

---

> **문서 이력**
>
> | 버전 | 날짜 | 변경 내용 |
> |------|------|-----------|
> | v1.0 | 2026-02-10 | 최초 작성 |
> | v2.0 | 2026-02-11 | 비즈니스급 품질 전면 개편. 세션 인증, 모던 JS 프론트엔드 |
> | v3.0 | 2026-02-11 | 프로젝트명 "페이라인(PayLine)"로 변경. Java 21 + Spring Boot 4.0.2 최신 스택 적용. 프론트엔드 Vue 3로 변경. 전체 의존성 2026년 2월 기준 최신화 |
