package com.origemite.lib.common.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Set;

/**
 * 공통 RestClient 로그.
 * 기본은 method/url/status/elapsed 한 줄이고, 설정으로 헤더·본문을 추가할 수 있다.
 */
public class CommonRestClientLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(CommonRestClientLoggingInterceptor.class);
    private static final int BODY_LOG_LIMIT = 2048;
    private static final Set<String> SENSITIVE_HEADERS = Set.of(
            "authorization",
            "proxy-authorization",
            "cookie",
            "set-cookie",
            "x-api-key",
            "x-auth-token"
    );

    private final CommonRestClientProperties properties;

    public CommonRestClientLoggingInterceptor() {
        this(new CommonRestClientProperties());
    }

    public CommonRestClientLoggingInterceptor(CommonRestClientProperties properties) {
        this.properties = properties == null ? new CommonRestClientProperties() : properties;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        long start = System.nanoTime();
        ClientHttpResponse response = execution.execute(request, body);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000L;

        StringBuilder line = new StringBuilder();
        line.append("outbound ")
                .append(request.getMethod()).append(' ')
                .append(request.getURI())
                .append(" → ").append(response.getStatusCode().value())
                .append(" (").append(elapsedMs).append("ms)")
                .append(" traceId=").append(traceId())
                .append(" reqBytes=").append(body == null ? 0 : body.length);

        if (properties.isLoggingHeadersEnabled()) {
            line.append(" headers=").append(maskHeaders(request.getHeaders()));
        }
        if (properties.isLoggingBodyEnabled()) {
            line.append(" reqBody=").append(truncate(body));
            line.append(" resBody=").append(truncate(readResponseBody(response)));
        }

        log.info(line.toString());
        return response;
    }

    private static String maskHeaders(HttpHeaders headers) {
        HttpHeaders masked = new HttpHeaders();
        headers.forEach((name, values) -> {
            if (name != null && SENSITIVE_HEADERS.contains(name.toLowerCase(Locale.ROOT))) {
                masked.put(name, values.stream().map(v -> "***").toList());
            } else {
                masked.put(name, values);
            }
        });
        return masked.toString();
    }

    private static String truncate(@Nullable byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        String text = new String(bytes, StandardCharsets.UTF_8);
        if (text.length() <= BODY_LOG_LIMIT) {
            return text;
        }
        return text.substring(0, BODY_LOG_LIMIT) + "...(truncated)";
    }

    private static byte[] readResponseBody(ClientHttpResponse response) {
        try {
            return response.getBody().readAllBytes();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    @Nullable
    private static String traceId() {
        String traceId = MDC.get("traceId");
        return traceId != null ? traceId : "-";
    }
}
