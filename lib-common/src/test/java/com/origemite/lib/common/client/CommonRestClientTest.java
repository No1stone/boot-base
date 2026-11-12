package com.origemite.lib.common.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Tag("unit")
@DisplayName("단위 / CommonRestClient")
class CommonRestClientTest {

    private MockRestServiceServer lastServer;

    @Test
    @DisplayName("GET 쿼리와 헤더를 넘기고 2xx body와 status를 받는다")
    void getReturnsStatusAndBody() {
        CommonRestClient client = clientWithMock();
        MockRestServiceServer server = lastServer;

        server.expect(requestTo("http://localhost/items?q=book"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Request-Id", "r1"))
                .andRespond(withSuccess("{\"name\":\"book\"}", MediaType.APPLICATION_JSON));

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Request-Id", "r1");
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
        query.add("q", "book");

        @SuppressWarnings("rawtypes")
        CommonRestResponse<Map> response = client.get("/items", headers, query, Map.class);

        assertThat(response.is2xxSuccessful()).isTrue();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsEntry("name", "book");
        server.verify();
    }

    @Test
    @DisplayName("4xx도 예외 없이 status를 반환한다")
    void exchangeKeepsErrorStatus() {
        CommonRestClient client = clientWithMock();
        MockRestServiceServer server = lastServer;

        server.expect(requestTo("http://localhost/reserve"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CONFLICT)
                        .body("{\"code\":\"RESERVATION_OVERLAP\"}")
                        .contentType(MediaType.APPLICATION_JSON));

        @SuppressWarnings("rawtypes")
        CommonRestResponse<Map> response = client.post("/reserve", null, Map.of("slot", "1"), Map.class);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).containsEntry("code", "RESERVATION_OVERLAP");
        server.verify();
    }

    /** Mock 서버가 requestFactory를 소유하도록, 커스터마이즈 후 bind 한다. */
    private CommonRestClient clientWithMock() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost");
        CommonRestClientProperties properties = new CommonRestClientProperties();
        properties.setLoggingEnabled(false);
        new CommonRestClientCustomizer(properties).customize(builder);
        lastServer = MockRestServiceServer.bindTo(builder).build();
        return new CommonRestClient(builder.build());
    }
}
