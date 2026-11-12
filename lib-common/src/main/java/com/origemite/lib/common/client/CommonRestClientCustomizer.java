package com.origemite.lib.common.client;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClient;

/**
 * RestClient.Builder에 타임아웃·버퍼링·로깅을 붙인다.
 * 리트라이는 {@link CommonRestClient} 호출부에서 적용한다.
 * Boot가 만든 Builder를 넘기면 트레이스 전파를 유지할 수 있다.
 */
public class CommonRestClientCustomizer {

    private final CommonRestClientProperties properties;
    private final CommonRestClientLoggingInterceptor loggingInterceptor;

    public CommonRestClientCustomizer(CommonRestClientProperties properties) {
        this(properties, null);
    }

    public CommonRestClientCustomizer(
            CommonRestClientProperties properties,
            @Nullable CommonRestClientLoggingInterceptor loggingInterceptor
    ) {
        this.properties = properties == null ? new CommonRestClientProperties() : properties;
        this.loggingInterceptor = loggingInterceptor != null
                ? loggingInterceptor
                : new CommonRestClientLoggingInterceptor(this.properties);
    }

    public void customize(RestClient.Builder builder) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        builder.requestFactory(factory);
        if (properties.isBufferContent() || properties.isLoggingBodyEnabled()) {
            builder.bufferContent((uri, method) -> true);
        }
        if (properties.isLoggingEnabled()) {
            builder.requestInterceptor(loggingInterceptor);
        }
    }
}
