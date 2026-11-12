package com.origemite.lib.common.client;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClient;

/**
 * RestClient.Builder에 타임아웃·버퍼링·로깅을 붙인다.
 * Boot가 만든 Builder를 넘기면 트레이스 전파를 유지할 수 있다.
 */
public class CommonRestClientCustomizer {

    private final CommonRestClientProperties properties;
    private final CommonRestClientLoggingInterceptor loggingInterceptor;

    public CommonRestClientCustomizer(CommonRestClientProperties properties) {
        this(properties, new CommonRestClientLoggingInterceptor());
    }

    public CommonRestClientCustomizer(
            CommonRestClientProperties properties,
            @Nullable CommonRestClientLoggingInterceptor loggingInterceptor
    ) {
        this.properties = properties == null ? new CommonRestClientProperties() : properties;
        this.loggingInterceptor = loggingInterceptor;
    }

    public void customize(RestClient.Builder builder) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        builder.requestFactory(factory);
        builder.bufferContent((uri, method) -> true);
        if (properties.isLoggingEnabled() && loggingInterceptor != null) {
            builder.requestInterceptor(loggingInterceptor);
        }
    }
}
