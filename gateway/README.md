# Gateway 토큰 검증  

![img.png](images/img.png)


부하테스트 시나리오  
K6
<details> <summary>k6</summary>
t3 미디엄에 모든 자원 프로세스들이 운영되고있는환경에서  
base line 설정
<details><summary>base line</summary>
  
![img_1.png](images/img_1.png)  
![img_2.png](images/img_2.png)  
![img_3.png](images/img_3.png)  
짧은 시간으로 테스트해보면 200~300구간에서  
시간이 급등함. 200이상은 지연시간이 늘어남    
200vu 구간에서 1분으로 시간을 늘려서 테스트실행  
cpu는 안정적

베이스 최대 200vu로 테스트
![img_5.png](images/img_5.png)  
![img_4.png](images/img_4.png)  

</details>

<details><summary>jwt rsa 검증 부하테스트 </summary>

![img_6.png](images/img_6.png)   
![img_7.png](images/img_7.png)   
![img_8.png](images/img_8.png)  

50 -> 100vu 구간에서 시간 급증 cpu는 안정적
rsa 검증 로직을 간호화 할 필요가 있어보여서  
redis 에 access token을 ttl 처리하고 검증하는 로직으로 변경 필요해보임.

</details>


</details>


통합 시나리오 테스트   
postman
