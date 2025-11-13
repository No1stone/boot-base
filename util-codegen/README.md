# CONTROLLER / SERVICE / DTO / ENTITY / REPOSITORY / SPEC 생성


DB 스키마 조회 후 Controller, Service, Facade, Repository, Entity, JpaSpec 자동 생성

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
