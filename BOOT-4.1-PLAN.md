# Boot 4.1 진행 방안

> 브랜치: `java/boot-4.1` (작업) · `java/boot-3.5` (LTS, 삭제하지 않음)  
> 목표: 4.1에서 동작하는 정리된 코드가 **3.5로 되돌려도** 정상 동작한다.

## 전제

- Spring 의존성 표면은 크게 안 바꾸되, **3.x에서 deprecated → 4.0에서 제거**된 API를 먼저 걷는다.
- 공식 가이드상 4.0은 deprecation 제거 외에 Jackson 3, starter/모듈 분할, 일부 기능 제거도 있다.  
  양립을 깨는 변경은 단계마다 따로 기록하고, 가능하면 공통 API만 쓴다.
- 1차 범위: `lib-common` · `lib-model` · `api-auth` · `gateway`  
  exclude(saga/batch/graphql)는 이후.

## 순서

| 단계 | 내용 | 통과 기준 | 비고 |
|------|------|-----------|------|
| 0 | 준비·호환 목록 | 이 문서 + 마이그레이션 노트 링크 정리 | 버전 bump 없음 |
| 1 | **3.5 유지** 상태에서 deprecated API·설정 정리 | `lib-common`/`lib-model`/`api-auth`/`gateway` 컴파일·관련 단위 테스트 | 3.5에서도 동작해야 함 |
| 2 | 자동생성 검증 | `@BizEnum` clean 재생성, QueryDSL Q타입, 컴파일 | Processor·생성물 |
| 3 | Boot **4.1** BOM·플러그인만 올리기 | 동일 모듈 컴파일 | 깨지면 항목별로 기록 |
| 4 | 4.x 전용 깨짐 대응 (Jackson/starter/Cloud 등) | api-auth 단위 테스트·기동 | 양립 불가 시 분기 전략 합의 |
| 5 | gateway | 기동·CachePort·필터 | |
| 6 | auth↔gateway 통합 | 대표 시나리오 | |
| 7 | 3.5 교차 확인 | 같은 커밋/패치를 `java/boot-3.5` BOM으로 빌드 | **필수 게이트** |
| 8 | exclude 모듈 | 추후 | |

## 양립 규칙

- 4.1에서만 되는 API를 기본 경로에 넣지 않는다. 필요하면 합의 후 기록.
- 설정 키 rename은 양쪽에서 유효한 쪽을 우선하고, migrator는 임시만.
- 단계 끝마다 커밋. 한 단계 실패 시 그 단계만 되돌린다.

## 참고

- [Spring Boot 4.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide)
- [Spring Boot 4.1 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.1-Release-Notes)

## 단계 0 결과 (위험 목록)

스캔일: 2026-10-27. Boot는 아직 **3.5.16**. 코드 변경 없음.

| 우선 | 항목 | 위치 | 3.5 양립 메모 |
|------|------|------|----------------|
| 1 | `spring-test` **6.1.10 고정** | `lib-common/build.gradle` | BOM과 어긋남. 단계 1에서 핀 제거·BOM 위임 후보 |
| 2 | Jackson2 API 직접 사용 | `WebConfig`, `TransformUtils`, serializers, RedisConfig, Feign | 4.x Jackson 3 영향. 단계 1에서는 **동작 유지**, 4.1 bump 후 대응 |
| 3 | `spring-boot-starter-web` | lib-common, lib-model, api-auth | 4.x에서 `webmvc` 이름 전환 가능. bump 단계 |
| 4 | Security | `SecurityFilterChain` 이미 사용 | 구 `WebSecurityConfigurerAdapter` 없음. 양호 |
| 5 | 테스트 | 대부분 Mockito 단위 테스트 | `@MockBean` 거의 없음. 양호 |
| 6 | 자동생성 | `BizEnumProcessor`, QueryDSL | 단계 2에서 clean 재생성 |
| 7 | Cloud / Vault / gRPC / Redisson | api-auth, gateway | 단계 3~5에서 버전 매트릭스 |

### 단계 1에서 할 일 (다음)

1. `lib-common`의 `spring-test` 버전 핀 제거(또는 test 범위로 정리) 후 컴파일·관련 테스트.
2. 컴파일 경고에서 잡히는 3.x deprecated API만 최소 수정(양립 깨는 리팩터 금지).
3. Jackson/starter rename은 **아직 하지 않음**.
