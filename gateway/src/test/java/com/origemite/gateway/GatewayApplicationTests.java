package com.origemite.gateway;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("전체 컨텍스트는 Redis·upstream 등 local 인프라 필요. bootRun 스모크로 대체.")
@SpringBootTest
class GatewayApplicationTests {

    @Test
    void contextLoads() {
    }

}
