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

import java.time.Duration;
import java.util.List;
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

    @Test
    @DisplayName("기본 타임아웃·리트라이·로깅 옵션 값이 있다")
    void defaultTimeoutAndRetry() {
        CommonRestClientProperties properties = new CommonRestClientProperties();
        assertThat(properties.getConnectTimeout().getSeconds()).isEqualTo(1);
        assertThat(properties.getReadTimeout().getSeconds()).isEqualTo(1);
        assertThat(properties.isRetryEnabled()).isTrue();
        assertThat(properties.getRetryMaxAttempts()).isEqualTo(2);
        assertThat(properties.getRetryWait().toMillis()).isEqualTo(200);
        assertThat(properties.isLoggingBodyEnabled()).isFalse();
        assertThat(properties.isLoggingHeadersEnabled()).isFalse();
        assertThat(properties.isBufferContent()).isTrue();
        assertThat(properties.isRetryOnServerError()).isFalse();
        assertThat(properties.getRetryStatusCodes()).isEmpty();
    }

    @Test
    @DisplayName("retryOnServerError 이면 5xx 후 재시도한다")
    void retriesServerErrorWhenEnabled() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost");
        CommonRestClientProperties properties = new CommonRestClientProperties();
        properties.setLoggingEnabled(false);
        properties.setRetryEnabled(true);
        properties.setRetryMaxAttempts(2);
        properties.setRetryWait(Duration.ofMillis(10));
        properties.setRetryOnServerError(true);
        new CommonRestClientCustomizer(properties).customize(builder);
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        CommonRestClient client = new CommonRestClient(builder.build(), properties);

        server.expect(requestTo("http://localhost/flaky"))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));
        server.expect(requestTo("http://localhost/flaky"))
                .andRespond(withSuccess("{\"ok\":true}", MediaType.APPLICATION_JSON));

        @SuppressWarnings("rawtypes")
        CommonRestResponse<Map> response = client.get("/flaky", null, null, Map.class);

        assertThat(response.is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).containsEntry("ok", true);
        server.verify();
    }

    @Test
    @DisplayName("retryStatusCodes 에 넣은 코드만 재시도한다")
    void retriesConfiguredStatusCodes() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost");
        CommonRestClientProperties properties = new CommonRestClientProperties();
        properties.setLoggingEnabled(false);
        properties.setRetryEnabled(true);
        properties.setRetryMaxAttempts(2);
        properties.setRetryWait(Duration.ofMillis(10));
        properties.setRetryStatusCodes(List.of(429));
        new CommonRestClientCustomizer(properties).customize(builder);
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        CommonRestClient client = new CommonRestClient(builder.build(), properties);

        server.expect(requestTo("http://localhost/limited"))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));
        server.expect(requestTo("http://localhost/limited"))
                .andRespond(withSuccess("{\"ok\":true}", MediaType.APPLICATION_JSON));

        @SuppressWarnings("rawtypes")
        CommonRestResponse<Map> response = client.get("/limited", null, null, Map.class);

        assertThat(response.is2xxSuccessful()).isTrue();
        server.verify();
    }

    /** Mock 서버가 requestFactory를 소유하도록, 커스터마이즈 후 bind 한다. */
    private CommonRestClient clientWithMock() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost");
        CommonRestClientProperties properties = new CommonRestClientProperties();
        properties.setLoggingEnabled(false);
        properties.setRetryEnabled(false);
        new CommonRestClientCustomizer(properties).customize(builder);
        lastServer = MockRestServiceServer.bindTo(builder).build();
        return new CommonRestClient(builder.build(), properties);
    }
}
