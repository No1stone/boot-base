# CONTROLLER / SERVICE / DTO / ENTITY / REPOSITORY / SPEC 등 생성

# 개발목적
AI 코딩툴을 병행해도
프로젝트 규모가 커지고 모듈이 많아지면,
코드 컨벤션과 레이어 전체의 일관성이 쉽게 깨지는 문제가 있었습니다.

이 반복적인 문제를 줄이기 위해
개인적으로 사용하던 자동 생성 스크립트를 확장해
팀 컨벤션 기반 Code Generator를 구축했습니다.

처음에는 혼자 편하게 쓰려고 만든 간단한 도구였지만,
실전에서는 이 방식이
가장 빠르고 정확하게 반복 작업을 제거하고
대규모 모듈 구조에서도 안정적으로 일관성을 유지하는 효과를 얻었습니다

<details> 

DB 스키마 조회 후 Controller, Service, Repository, Entity, JpaSpec 자동 생성

- {EntityName}Controller
- {EntityName}Service
- {EntityName}Repository
- {EntityName}Entity
- {EntityName}JpaSpec
- {EntityName}ControllerTest
- {EntityName}ServiceTest


REST 엔드포인트 규칙:

메서드	경로	설명  
GET	/	목록 조회  
POST	/	생성  
GET	/{id}	단건 조회  
PUT	/{id}	수정  
DELETE	/{id}	삭제  
GET	/find-all	전체 조회  
GET	/find-by-ids	다건 조회  
DELETE	/delete-by-ids	다건 삭제  
GET	/search-name	이름 기반 페이지 조회  
GET	/find-all-name	이름 전체 조회  
규칙을 기반으로 service repo,entity, spec dto 기본생성  
연관관계는 직접 구현.

컨트롤러 프리픽스는 네이밍출동 방지를 위한 UUID 생성 endpoint 수정필요   
예: @RequestMapping("/b4dd4878")  


이 구조를 기반으로, 신규 테이블을 추가할 때마다 **반자동으로 기본   
CRUD API + 테스트 골격**까지 한 번에 생성하는 것을 목표로 합니다.
</details>