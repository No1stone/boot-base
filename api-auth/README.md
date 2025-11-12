# Vault Transit 기반 JWT 키 관리 & 회전 시스템

> Origemite 인프라 표준 — Spring Auth / Gateway / Jenkins / AWS 기반  
> 보안·운영·DevSecOps 연동 Vault 설정 

---

##  1. 개요

이 문서는 **Vault Transit 엔진**을 사용하여
- JWT 서명 키를 안전하게 보관하고
- Auth 서버가 Vault를 통해 서명(JWT 발급)을 수행하며
- Jenkins 배치로 **자동 키 회전(Key Rotation)** 을 실행하는 구조를 정의한다.

**Vault는 절대 개인키를 외부로 노출하지 않으며**,  
Auth 서버는 Vault에서 공개키를 JWKS(JSON Web Key Set)로 변환해 Gateway에 제공한다.


| 목적          | 명령어                                                  |
| ----------- | ---------------------------------------------------- |
| 컨테이너 접속     | `docker exec -it vault sh`                           |
| Vault 상태 확인 | `vault status`                                       |
| Transit 활성화 | `vault secrets enable transit`                       |
| 키 생성        | `vault write -f transit/keys/auth-sig type=rsa-2048` |
| 키 조회        | `vault read transit/keys/auth-sig`                   |
| 키 회전        | `vault write -f transit/keys/auth-sig/rotate`        |
| 서명 테스트      | `vault write transit/sign/auth-sig input=<base64>`   |


```text
docker exec -it vault /bin/sh
# 컨테이너 쉘에서
export VAULT_ADDR=http://127.0.0.1:8200   
export VAULT_TOKEN=root                   

vault status
```

![img.png](images/img.png)   

![img_1.png](images/img_1.png)

![img_2.png](images/img_2.png)   

![img_3.png](images/img_3.png)   

![img_4.png](images/img_4.png)  



