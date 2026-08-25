# 🥩 MeetRoute
## "정보 검색을 없애고 사람을 남기다."
축산 영업사원을 위한 지역별 음식점 데이터베이스 형상 관리 시스템

## 📢 핵심 기능
- 서울시 공공데이터(일반음식점 인허가 정보)를 지역구 단위로 가공해 거래처 후보를 우선순위순으로 리스트/지도로 보여주고 영업 이력 관리합니다.
- 매장 상세 정보와 사용자가 남긴 영업 메모를 기반으로 매장별 맞춤 영업 전략 AI 브리핑을 생성합니다.

## 📡 시스템 아키텍처
<img width="1111" height="764" alt="Image" src="https://github.com/user-attachments/assets/0df57edb-e4a6-4a8f-8f44-a2f604cc74dc" />

## 🛠 기술 스택

| 구분 | 내용 |
|---|---|
| Language / Runtime | Java 17 |
| Framework | Spring Boot 4.1.1 |
| Data | Spring Data JPA, MySQL (AWS RDS) |
| Auth | Spring Security (세션 기반, permitAll + 서버 세션 검증) |
| AI | Spring AI (Anthropic, `claude-haiku-4-5`) |
| API 문서 | springdoc-openapi (Swagger UI) 2.8.0 |
| 빌드 | Gradle (Wrapper) |
| 배포 | AWS EC2 + RDS |

## 🗂️ 프로젝트 구조

```
src/main/java/com/unithon/meetroute
├── MeetrouteApplication.java
│
├── domain
│   ├── auth                     # 회원가입 / 로그인 / 세션
│   │   ├── controller/AuthController.java
│   │   ├── service/AuthService.java
│   │   ├── dto/                 # LoginRequest, LoginResponse, SignupRequest, SignupResponse
│   │   └── SessionConst.java
│   │
│   ├── shop                     # 매장(거래처 후보) 조회 + AI 영업 브리핑
│   │   ├── controller/ShopController.java
│   │   ├── service/ShopService.java
│   │   ├── entity/Shop.java
│   │   ├── repository/          # ShopRepository, ShopSpecifications
│   │   ├── dto/                 # ShopListItemResponse, ShopDetailResponse, ShopMapMarkerResponse
│   │   └── briefing/            # AI 브리핑 생성 (Claude API 호출)
│   │       ├── service/ShopBriefingService.java
│   │       └── dto/BriefingResponse.java
│   │
│   ├── salesActivity             # 매장 방문 이력 / 메모
│   │   ├── controller/SalesActivityController.java
│   │   ├── service/SalesActivityService.java
│   │   ├── entity/               # SalesActivity, VisitStatus
│   │   ├── repository/SalesActivityRepository.java
│   │   └── dto/                  # RecordVisitRequest, UpdateMemoRequest, SalesActivityResponse
│   │
│   ├── priority                  # 매장별 영업 우선순위 계산
│   │   ├── controller/           # PriorityController, PriorityBatchController
│   │   ├── service/              # PriorityService, PriorityBatchService, PriorityScoreCalculator
│   │   ├── entity/                # Priority, PriorityGrade
│   │   ├── repository/PriorityRepository.java
│   │   └── dto/                   # PriorityResponse, PriorityBatchResponse
│   │
│   └── user                       # 영업사원 계정
│       ├── entity/User.java
│       └── repository/UserRepository.java
│
└── global
    ├── config/                    # SecurityConfig, SwaggerConfig, ChatClientConfig
    ├── response/ApiResponse.java  # 공통 응답 래퍼
    └── exception/                 # ErrorCode, BusinessException, GlobalExceptionHandler
```

도메인별로 `controller / service / repository / entity / dto`를 함께 묶는 패키지 구조를 따릅니다.

## 📁 도메인 모델

- **Shop** — 서울시 공공데이터 기반 음식점(거래처 후보). 이름/주소/지역구/업태/좌표 등을 가짐.
- **User** — 영업사원 계정.
- **SalesActivity** — 사용자(영업사원)의 방문 이력(방문 회차, 메모, 방문일).
- **Priority** — 매장별 영업 우선순위 점수/등급 계산 결과

## 📄 API

Swagger UI: `/swagger-ui/index.html`

| 메서드 | 경로 | 설명 |
|---|---|---|
| POST | `/api/v1/auth/signup` | 영업사원 회원가입 |
| POST | `/api/v1/auth/login` | 로그인, 세션 발급 |
| POST | `/api/v1/auth/logout` | 로그아웃 |
| GET | `/api/v1/shops` | 매장 목록 조회 (지역구/업태/등급 필터, 페이징) |
| GET | `/api/v1/shops/map` | 지도 bounding box 내 매장 조회 |
| GET | `/api/v1/shops/{shopId}` | 매장 상세 조회 |
| POST | `/api/v1/shops/{shopId}/briefing` | AI 영업 브리핑 생성 (로그인 필요) |
| POST | `/api/v1/shops/{shopId}/sales_activity` | 매장 방문 기록 |
| POST | `/api/v1/shops/{shopId}/sales_activity/memo` | 방문 메모 기록 |
| GET | `/api/v1/shops/{shopId}/sales_activity` | 방문 이력 목록 조회 |
| GET | `/api/v1/shops/{shopId}/priority` | 매장 우선순위 조회 |
| POST | `/api/v1/shops/{shopId}/priority` | 매장 우선순위 재계산 |
| POST | `/api/v1/priorities/batch` | 전체 매장 우선순위 일괄 계산 |

모든 응답은 아래 형태로 통일된 `ApiResponse<T>`로 감싸서 반환합니다.

```json
{
  "success": true,
  "code": null,
  "message": "OK",
  "data": { ... }
}
```
