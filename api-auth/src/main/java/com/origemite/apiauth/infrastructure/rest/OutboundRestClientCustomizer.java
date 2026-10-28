package com.origemite.apiauth.infrastructure.rest;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/** RestClient.Builder에 타임아웃·한 줄 로그. 인증 헤더 없음. */
public class OutboundRestClientCustomizer {

    private final OutboundRestClientProperties properties;
    private final OutboundRestClientLoggingInterceptor loggingInterceptor;

    public OutboundRestClientCustomizer(
            OutboundRestClientProperties properties,
            OutboundRestClientLoggingInterceptor loggingInterceptor
    ) {
        this.properties = properties;
        this.loggingInterceptor = loggingInterceptor;
    }

    public void customize(RestClient.Builder builder) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        builder.requestFactory(factory);
        if (properties.isLoggingEnabled()) {
            builder.requestInterceptor(loggingInterceptor);
        }
    }
}
