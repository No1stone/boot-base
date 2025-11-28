# 본 프로젝트는 netflex dgs 학습 프로젝트 입니다.

https://github.com/Netflix/dgs-framework/blob/master/graphql-dgs-client/build.gradle.kts

netflix    
https://youtu.be/XpunFFS-n8I  
단순 gc 업그레이드만으로 성능20%향상     
넷플릭스가 rxJava(webflux)를 사용안하는 이유, 처리방법    
  
우리팀에서의 이유는 리소스 부족 및 안정성  
넷플릭스 리소스 낭비 / 안정성과 속도를 dgs 로 개선.

Domain Graog Service

## REST api 와 gRPC 차이
<details><summary> rest vs grpc </summary>

http1.1, http2 차이  
network 레이턴씨 부분,  
json으로 변경하면서 생기는 리소스들, 
</details>   
  
## java, gRPC 데이터타입 
<details><summary>data type</summary>  

| Java Type | Proto Type | 설명         |
| --------- | ---------- | ---------- |
| `int`     | `int32`    | 32비트 정수    |
| `long`    | `int64`    | 64비트 정수    |
| `boolean` | `bool`     | true/false |
| `double`  | `double`   | 64비트 부동소수  |
| `float`   | `float`    | 32비트 부동소수  |
| `String`  | `string`   | UTF-8 문자열  |
| `byte[]`  | `bytes`    | 바이너리 데이터   |

</details>   
  
## gRPC 패턴
<details><summary>gRPC 패턴 </summary>  

표작성 필요  
unary  
server streaming  
client streaming  
bidirectional streaming  
  
</details>  

## 서버구성
<details><summary> 서버구성 </summary>

draw.io로 작성필요  
nginx  
gateway   
eureka  
grpc  
api-auth  

</details>  
  
## Netflex dgs 흐름
<details><summary>dgs 흐름 </summary>

</details>  

## Postman gRPC 사용법
<details> <summary> Postman gRPC 사용 </summary> </details>

## Test 성능테스트
<details> <summary> 테스트 </summary>

아파치 벤치마킹  
ghz  

</details>

