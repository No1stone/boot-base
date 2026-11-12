package com.origemite.lib.common.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;

import java.io.IOException;

/**
 * 공통 RestClient 한 줄 로그. 본문은 읽지 않는다(호출부 exchange가 소비).
 */
public class CommonRestClientLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(CommonRestClientLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        long start = System.nanoTime();
        ClientHttpResponse response = execution.execute(request, body);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000L;
        log.info("outbound {} {} → {} ({}ms) traceId={} reqBytes={}",
                request.getMethod(),
                request.getURI(),
                response.getStatusCode().value(),
                elapsedMs,
                traceId(),
                body == null ? 0 : body.length);
        return response;
    }

    @Nullable
    private static String traceId() {
        String traceId = MDC.get("traceId");
        return traceId != null ? traceId : "-";
    }
}
